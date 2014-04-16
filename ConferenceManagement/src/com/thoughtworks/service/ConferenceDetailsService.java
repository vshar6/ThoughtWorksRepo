package com.thoughtworks.service;

import java.util.List;

import com.thoughtworks.dto.ConferenceDetails;

public interface ConferenceDetailsService {

	/**
	 * Gets the talk list from file.
	 *
	 * @param fileName the file name
	 * @return the talk list from file
	 */
	List<String> getTalkListFromFile(String fileName);

	/**
	 * Validate and create talks.
	 *
	 * @param confDetails the conf details
	 * @return the list
	 * @throws Exception the exception
	 */
	List<ConferenceDetails> validateAndCreateTalks(List<String> confDetails)
			throws Exception;

}
