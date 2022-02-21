package com.challenge.disneyworld.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.disneyworld.dto.ModelRegistrationUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
public class MailService {

    @Autowired
    SendGrid sendGrid;

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	public String send(ModelRegistrationUser user) throws IOException {
		// the sender email should be the same as we used to Create a Single Sender Verification
		Email from = new Email("ianklebold@gmail.com");
		Email to = new Email(user.getEmail());
		Mail mail = new Mail();
        // we create an object of our static class feel free to change the class on it's own file 
        // I try to keep every think simple
		DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
		personalization.addTo(to);
		mail.setFrom(from);
		mail.setSubject("Disney World");
        // This is the first_name variable that we created on the template
		personalization.addDynamicTemplateData("username", user.getUsername());
        mail.addPersonalization(personalization);
		mail.setTemplateId("d-dfcbd8322cbb4db5b38dd9f0ae967f0f");
		// this is the api key
		
		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sendGrid.api(request);
			 logger.info(response.getBody());
			return response.getBody();
		} catch (IOException ex) {
			throw ex;
		}
	}

	// This class handels the dynamic data for the template
	// Feel free to customise this class our to putted on other file 
	private static class DynamicTemplatePersonalization extends Personalization {

		@JsonProperty(value = "dynamic_template_data")
		private Map<String, Object> dynamic_template_data;

		@JsonProperty("dynamic_template_data")
		public Map<String, Object> getDynamicTemplateData() {
			if (dynamic_template_data == null) {
				return Collections.<String, Object>emptyMap();
			}
			return dynamic_template_data;
		}

		public void addDynamicTemplateData(String key, String value) {
			if (dynamic_template_data == null) {
				dynamic_template_data = new HashMap<String, Object>();
				dynamic_template_data.put(key, value);
			} else {
				dynamic_template_data.put(key, value);
			}
		}

	}
}
