package com.thoughtworks.service;

import java.util.List;

import com.thoughtworks.dto.ConferenceDetails;

public interface ProcessConferenceScedules {
	
	/**
	 * Gets the schedule conference track.
	 *
	 * @param conference the conference
	 * @return the schedule conference track
	 * @throws Exception the exception
	 */
	List<List<ConferenceDetails>> getScheduleConferenceTrack(
			List<ConferenceDetails> conference) throws Exception;
}
