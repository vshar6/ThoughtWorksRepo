package com.thoughtworks.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.thoughtworks.dto.ConferenceDetails;
import com.thoughtworks.service.ProcessConferenceScedules;

// TODO: Auto-generated Javadoc
/**
 * The Class ProcessConferenceSchedules.
 */
public class ProcessConferenceSchedulesImpl implements ProcessConferenceScedules {

	/** The Constant _EVENING_SESSION_DURATION. */
	private static final int _EVENING_SESSION_DURATION = 240;

	/** The Constant _MORNING_SESSION_DURATION. */
	private static final int _MORNING_SESSION_DURATION = 180;
	/** The Constant NETWORKING_EVENT. */
	private static final String NETWORKING_EVENT = "Networking Event";
	/** The log. */
	private static final Logger LOG = Logger.getLogger(ProcessConferenceSchedulesImpl.class);
	
	
	/**
	 * Schedule Conference tracks for morning and evening session.
	 * 
	 * @param conference
	 *            the conference
	 * @return the schedule conference track
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public List<List<ConferenceDetails>> getScheduleConferenceTrack(
			List<ConferenceDetails> conference) throws Exception {
		// Find the total possible days.
		int perDayMinTime = 6 * 60;
		int totalTalksTime = getTotalConferenceTime(conference);
		int totalPossibleDays = totalTalksTime / perDayMinTime;

		LOG.info("Total possible days:"+totalPossibleDays);
		List<ConferenceDetails> conferenceDetailList = new ArrayList<ConferenceDetails>();
		conferenceDetailList.addAll(conference);
		Collections.sort(conferenceDetailList);

		// Find possible combinations for the morning session.
		List<List<ConferenceDetails>> morningConferences = findPossibleCombSession(
				conferenceDetailList, totalPossibleDays, true);
		

		// Remove all the scheduled conferences for morning session, from the
		// operationList.
		for (List<ConferenceDetails> conferences : morningConferences) {
			LOG.info("morning conferences:"+conferences);
			conferenceDetailList.removeAll(conferences);
		}

		// Find possible combinations for the evening session.
		List<List<ConferenceDetails>> combForEveSessions = findPossibleCombSession(
				conferenceDetailList, totalPossibleDays, false);

		// Remove all the scheduled talks for evening session, from the
		// operationList.
		for (List<ConferenceDetails> eveningConferences : combForEveSessions) {
			LOG.info("eveningConferences :"+eveningConferences);
			conferenceDetailList.removeAll(eveningConferences);
		}

		// check if the operation list is not empty, then try to fill all the
		// remaining talks in evening session.
		int maxSessionTimeLimit = _EVENING_SESSION_DURATION;
		if (!conferenceDetailList.isEmpty()) {
			List<ConferenceDetails> scheduledTalkList = new ArrayList<ConferenceDetails>();
			for (List<ConferenceDetails> talkList : combForEveSessions) {
				int totalTime = getTotalConferenceTime(talkList);

				for (ConferenceDetails talk : conferenceDetailList) {
					int talkTime = talk.getDuration();

					if (talkTime + totalTime <= maxSessionTimeLimit) {
						talkList.add(talk);
						talk.setScheduled(true);
						scheduledTalkList.add(talk);
					}
				}

				conferenceDetailList.removeAll(scheduledTalkList);
				if (conferenceDetailList.isEmpty())
					break;
			}
		}

		// If operation list is still not empty, its mean the conference can not
		// be scheduled with the provided data.
		if (!conferenceDetailList.isEmpty()) {
			throw new Exception("Unable to schedule all task for conferencing.");
		}

		// Schedule the day event from morning session and evening session.
		return getScheduledTalksList(morningConferences, combForEveSessions);
	}

	/**
	 * Find possible comb session.
	 * 
	 * @param conferenceList
	 *            the talks list for operation
	 * @param totalPossibleDays
	 *            the total possible days
	 * @param morningSession
	 *            the morning session
	 * @return the list
	 */
	@Override
	public List<List<ConferenceDetails>> findPossibleCombSession(
			List<ConferenceDetails> conferenceList, int totalPossibleDays,
			boolean morningSession) {
		int minSessionTimeLimit = _MORNING_SESSION_DURATION;
		int maxSessionTimeLimit = _EVENING_SESSION_DURATION;

		maxSessionTimeLimit = morningSession ? minSessionTimeLimit
				: maxSessionTimeLimit;

		int confListSize = conferenceList.size();
		List<List<ConferenceDetails>> conferenceCombinations = new ArrayList<List<ConferenceDetails>>();
		int possibleCombinationCount = 0;

		// Loop to get combination for total possible days.
		// Check one by one from each ConferenceDetails to get possible
		// combination.
		for (int count = 0; count < confListSize; count++) {
			int startPoint = count;
			int totalTime = 0;
			List<ConferenceDetails> possibleCombinationList = new ArrayList<ConferenceDetails>();

			// Loop to get possible combination.
			totalTime = updateListAndGetfinalTime(conferenceList, morningSession,
					minSessionTimeLimit, maxSessionTimeLimit, confListSize,
					startPoint, totalTime, possibleCombinationList);

			boolean validSession = false;
			if (morningSession) {
				validSession = (totalTime == maxSessionTimeLimit);
			} else {
				validSession = (totalTime >= minSessionTimeLimit && totalTime <= maxSessionTimeLimit);
			}

			// If session is valid than add this session in the possible
			// combination list and set all added talk as scheduled.
			if (validSession) {
				conferenceCombinations.add(possibleCombinationList);
				for (ConferenceDetails talk : possibleCombinationList) {
					talk.setScheduled(true);
				}
				possibleCombinationCount++;
				if (possibleCombinationCount == totalPossibleDays)
					break;
			}
		}

		return conferenceCombinations;
	}

	private int updateListAndGetfinalTime(List<ConferenceDetails> conferenceList,
			boolean morningSession, int minSessionTimeLimit,
			int maxSessionTimeLimit, int confListSize, int startPoint,
			int totalTime, List<ConferenceDetails> possibleCombinationList) {
		while (startPoint != confListSize) {
			int currentCount = startPoint;
			startPoint++;
			ConferenceDetails currentConference = conferenceList
					.get(currentCount);
			if (currentConference.isScheduled()) {
				continue;
			}
			int talkTime = currentConference.getDuration();
			// If the current talk time is greater than maxSessionTimeLimit
			// or
			// sum of the current time and total of talk time added in list
			// is greater than maxSessionTimeLimit.
			// then continue.
			if (talkTime > maxSessionTimeLimit
					|| talkTime + totalTime > maxSessionTimeLimit) {
				continue;
			}

			possibleCombinationList.add(currentConference);
			totalTime += talkTime;

			// If total time is completed for this session than break this
			// loop.
			if (morningSession) {
				if (totalTime == maxSessionTimeLimit) {
					break;
				}
			} else if (totalTime >= minSessionTimeLimit) {
				break;
			}
		}
		return totalTime;
	}

	/**
	 * Print the scheduled talks with the expected text msg.
	 * 
	 * @param combForMornSessions
	 *            the comb for morn sessions
	 * @param combForEveSessions
	 *            the comb for eve sessions
	 * @return the scheduled talks list
	 */
	private List<List<ConferenceDetails>> getScheduledTalksList(
			List<List<ConferenceDetails>> combForMornSessions,
			List<List<ConferenceDetails>> combForEveSessions) {
		List<List<ConferenceDetails>> scheduledTalksList = new ArrayList<List<ConferenceDetails>>();
		int totalPossibleDays = combForMornSessions.size();

		// for loop to schedule event for all days.
		for (int dayCount = 0; dayCount < totalPossibleDays; dayCount++) {
			List<ConferenceDetails> talkList = new ArrayList<ConferenceDetails>();

			// Create a date and initialize start time 09:00 AM.
			final Date date = new Date();
			final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma ");
			date.setHours(9);
			date.setMinutes(0);
			date.setSeconds(0);

			
			String scheduledTime = dateFormat.format(date);
			int trackCount = dayCount + 1;
			LOG.info("Track " + trackCount + ":");

			// Morning Session - set the scheduled time in the ConferenceDetails
			// and get the next time using time duration of current
			// ConferenceDetails.
			List<ConferenceDetails> mornSessionTalkList = combForMornSessions
					.get(dayCount);
			for (ConferenceDetails conferenceDetails : mornSessionTalkList) {
				conferenceDetails.setScheduledTime(scheduledTime);
				LOG.info(scheduledTime + conferenceDetails.getTitle());
				scheduledTime = getNextScheduledTime(date,
						conferenceDetails.getDuration());
				talkList.add(conferenceDetails);
			}

			// Scheduled Lunch Time for 60 mins.
			int lunchTimeDuration = 60;
			ConferenceDetails lunchTalk = new ConferenceDetails("Lunch",
					"Lunch", 60);
			lunchTalk.setScheduledTime(scheduledTime);
			talkList.add(lunchTalk);
			LOG.info(scheduledTime + "Lunch");

			// Evening Session - set the scheduled time in the talk and get the
			// next time using time duration of current talk.
			scheduledTime = getNextScheduledTime(date, lunchTimeDuration);
			List<ConferenceDetails> eveSessionTalkList = combForEveSessions
					.get(dayCount);
			for (ConferenceDetails talk : eveSessionTalkList) {
				talk.setScheduledTime(scheduledTime);
				talkList.add(talk);
				LOG.info(scheduledTime + talk.getTitle());
				scheduledTime = getNextScheduledTime(date, talk.getDuration());
			}

			// Scheduled Networking Event at the end of session, Time duration
			// is just to initialize the Talk object.
			ConferenceDetails networkingTalk = new ConferenceDetails(
					NETWORKING_EVENT, NETWORKING_EVENT, 60);
			networkingTalk.setScheduledTime(scheduledTime);
			talkList.add(networkingTalk);
			LOG.info(scheduledTime + "Networking Event\n");
			scheduledTalksList.add(talkList);
		}

		return scheduledTalksList;
	}

	/**
	 * Gets the next scheduled time.
	 * 
	 * @param date
	 *            the date
	 * @param timeDuration
	 *            the time duration
	 * @return the next scheduled time
	 */
	private String getNextScheduledTime(Date date, int timeDuration) {
		long timeInLong = date.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma ");

		long timeDurationInLong = timeDuration * 60 * 1000;
		long newTimeInLong = timeInLong + timeDurationInLong;

		date.setTime(newTimeInLong);
		String str = dateFormat.format(date);
		return str;
	}

	

	/**
	 * To get total time of talks of the given list.
	 * 
	 * @param confList
	 *            the conf list
	 * @return the total conference time
	 */
	private int getTotalConferenceTime(List<ConferenceDetails> confList) {
		if (confList == null || confList.isEmpty())
			return 0;

		int totalTime = 0;
		for (ConferenceDetails confDetail : confList) {
			totalTime += confDetail.getDuration();
		}
		return totalTime;
	}

}
