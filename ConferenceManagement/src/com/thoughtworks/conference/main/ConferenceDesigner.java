package com.thoughtworks.conference.main;

import java.util.List;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.thoughtworks.configuration.ConfigurationService;
import com.thoughtworks.dto.ConferenceDetails;
import com.thoughtworks.service.impl.ConferenceDetailsServiceImpl;
import com.thoughtworks.service.impl.ProcessConferenceSchedulesImpl;

public class ConferenceDesigner {

	private static final String SEPARATOR = "/";

	/** The Constant FILE_NAME. */
	private static final String FILE_NAME = "fileName";

	/** The Constant LOCATION. */
	private static final String LOCATION = "fileLocation";

	/** The log. */
	private static final Logger LOG = Logger
			.getLogger(ConferenceDesigner.class);

	public static void main(String[] args) {
		try {

			Properties prop = ConfigurationService.loadProperties();
			ConferenceDetailsServiceImpl conferenceDetailsService = new ConferenceDetailsServiceImpl();
			String filePath = SEPARATOR + prop.getProperty(LOCATION)
					+ SEPARATOR + prop.getProperty(FILE_NAME);
			LOG.info("Location in workspace--:" + filePath);
			List<String> talkListFromFile = conferenceDetailsService
					.getTalkListFromFile(filePath);

			List<ConferenceDetails> confDetailList = conferenceDetailsService
					.validateAndCreateTalks(talkListFromFile);
			if (LOG.isDebugEnabled()) {
				LOG.debug("List:" + confDetailList);
			}

			if (CollectionUtils.isNotEmpty(confDetailList)) {
				ProcessConferenceSchedulesImpl processConferenceSchedules = new ProcessConferenceSchedulesImpl();
				processConferenceSchedules
						.getScheduleConferenceTrack(confDetailList);
			} else {
				LOG.info("Talk list is empty:"+confDetailList);
			}

		} catch (Exception e) {
			LOG.error("Exception in ConferenceDesigner", e);
		}

	}

}
