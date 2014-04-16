package com.thoughtworks.dto;

// TODO: Auto-generated Javadoc
/**
 * The Class ConferenceDetails.
 */
public class ConferenceDetails implements Comparable<ConferenceDetails>{
	
	 
        /** The title. */
        private String title;
        
        /** The name. */
        private String name;
        
        /** The duration. */
        int duration;
        
        /** The scheduled. */
        private boolean scheduled = false;
        
        /** The scheduled time. */
        private String scheduledTime;
       
        public ConferenceDetails(String title, String name, int time) {
            this.title = title;
            this.name = name;
            this.duration = time;
        }
        
        public ConferenceDetails() {
		
		}

		/**
         * Gets the title.
         *
         * @return the title
         */
        public String getTitle() {
			return title;
		}

		/**
		 * Sets the title.
		 *
		 * @param title the new title
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets the name.
		 *
		 * @param name the new name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Gets the duration.
		 *
		 * @return the duration
		 */
		public int getDuration() {
			return duration;
		}

		/**
		 * Sets the duration.
		 *
		 * @param duration the new duration
		 */
		public void setDuration(int duration) {
			this.duration = duration;
		}

		/**
		 * Checks if is scheduled.
		 *
		 * @return true, if is scheduled
		 */
		public boolean isScheduled() {
			return scheduled;
		}

		/**
		 * Sets the scheduled.
		 *
		 * @param scheduled the new scheduled
		 */
		public void setScheduled(boolean scheduled) {
			this.scheduled = scheduled;
		}

		/**
		 * Gets the scheduled time.
		 *
		 * @return the scheduled time
		 */
		public String getScheduledTime() {
			return scheduledTime;
		}

		/**
		 * Sets the scheduled time.
		 *
		 * @param scheduledTime the new scheduled time
		 */
		public void setScheduledTime(String scheduledTime) {
			this.scheduledTime = scheduledTime;
		}

		/**
		 * Sort data in decending order.
		 *
		 * @param obj the obj
		 * @return the int
		 */
        @Override
        public int compareTo(ConferenceDetails obj)
        {
        	
            if(this.duration > obj.duration)
                return -1;
            else if(this.duration < obj.duration)
                return 1;
            else
            return 0;
        }

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ConferenceDetails [title=" + title + ", name=" + name
					+ ", duration=" + duration + ", scheduled=" + scheduled
					+ ", scheduledTime=" + scheduledTime + "]";
		}

}
