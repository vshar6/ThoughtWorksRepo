package com.thoughtworks.conference.main;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.thoughtworks.configuration.ConfigurationService;
import com.thoughtworks.dto.ConferenceDetails;
import com.thoughtworks.service.impl.ConferenceDetailsServiceImpl;
import com.thoughtworks.service.impl.ProcessConferenceSchedulesImpl;

public class ConferenceDesigner {
	
	
	/** The Constant FILE_NAME. */
	private static final String FILE_NAME = "fileName";
	
	/** The log. */
	private static final Logger LOG = Logger.getLogger(ConferenceDesigner.class);
	
		public static void main(String[] args) {
			try {
				
				Properties prop = ConfigurationService.loadProperties();
				ConferenceDetailsServiceImpl readDetails = new ConferenceDetailsServiceImpl();
				List<String> talkListFromFile = readDetails
						.getTalkListFromFile(prop.getProperty(FILE_NAME));

				List<ConferenceDetails> confDetailList = readDetails
						.validateAndCreateTalks(talkListFromFile);
				// System.out.println("List:"+confDetailList);
				
				ProcessConferenceSchedulesImpl processConferenceSchedules = new ProcessConferenceSchedulesImpl();
				processConferenceSchedules.getScheduleConferenceTrack(confDetailList);


			} catch (Exception e) {
				LOG.error("Exception in ConferenceDesigner",e);
			}

		}
	

}
