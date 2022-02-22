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
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
public class MailService {

    @Autowired
    SendGrid sendGrid;

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	public String send(ModelRegistrationUser user) throws IOException {
		// El from es el que envia el correo. Debe ser el mismo con el que nos registramos en sendgrid
		Email from = new Email("ianklebold@gmail.com");
		Email to = new Email(user.getEmail());
		Mail mail = new Mail();

		//Configuracion del template
		DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
		personalization.addTo(to);
		mail.setFrom(from);
		mail.setSubject("Disney World");
        // Aca enviamos las variables que configuramos en el template
		personalization.addDynamicTemplateData("username", user.getUsername());
        mail.addPersonalization(personalization);
		//Indicamos el Id del template
		mail.setTemplateId("d-dfcbd8322cbb4db5b38dd9f0ae967f0f");
		
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
