package com.thoughtworks.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.thoughtworks.conference.main.ConferenceDesigner;
import com.thoughtworks.configuration.ConfigurationService;
import com.thoughtworks.dto.ConferenceDetails;
import com.thoughtworks.service.ConferenceDetailsService;

/**
 * The Class ConferenceDetailsServiceImpl.
 */
public class ConferenceDetailsServiceImpl implements ConferenceDetailsService {

	/** The Constant _FIVE. */
	private static final int _FIVE = 5;
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(ConferenceDetailsServiceImpl.class);


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thoughtworks.service.ConferenceDetailsService#getTalkListFromFile
	 * (java.lang.String)
	 */
	@Override
	public List<String> getTalkListFromFile(String fileName) {
		List<String> talkList = new ArrayList<String>();

		try {

			String filePath = new File(StringUtils.EMPTY).getAbsolutePath()+fileName;
			
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String strLine = br.readLine();
			// Read File Line By Line
			while (strLine != null) {
				talkList.add(strLine);
				strLine = br.readLine();
				// System.out.println(strLine);
			}

		} catch (Exception e) {
			LOG.error("Error in getTalkListFromFile:",e);
		}

		return talkList;
	}

	/**
	 * Validate talk list, check the time for talk and initilize Talk Object
	 * accordingly.
	 * 
	 * @param confDetails
	 *            the conf details
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public List<ConferenceDetails> validateAndCreateTalks(List<String> talkList)
			throws Exception {

		List<ConferenceDetails> validTalksList = new ArrayList<ConferenceDetails>();

		String min = "min";
		String lightning = "lightning";

		for (String conference : talkList) {
			ConferenceDetails confDetails = new ConferenceDetails();
			int whiteSpaceIndex = conference.lastIndexOf(" ");

			String name = conference.substring(0, whiteSpaceIndex);
			String timeStr = conference.substring(whiteSpaceIndex + 1);
			confDetails.setName(name);
			confDetails.setTitle(conference);
			
			// If title is missing or blank.
			if (name == null || StringUtils.EMPTY.equals(name.trim())) {

			} else {
				int duration = 0;
				// Parse time from the time string .
				try {
					if (timeStr.endsWith(min)) {
						duration = Integer.parseInt(timeStr.substring(0,
								timeStr.indexOf(min)));
					} else if (timeStr.endsWith(lightning)) {
						String lightningTime = timeStr.substring(0,
								timeStr.indexOf(lightning));
						duration = StringUtils.EMPTY.equals(lightningTime) ? 5
								: Integer.parseInt(lightningTime) * _FIVE;
					}
					confDetails.setDuration(duration);
				} catch (NumberFormatException nfe) {

				}

				// Add talk to the valid talk List.
				validTalksList.add(confDetails);
			}
		}

		return validTalksList;
	}

	

}
