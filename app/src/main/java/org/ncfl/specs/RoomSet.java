package org.ncfl.specs;

import com.fasterxml.jackson.annotation.JsonAlias;

@SuppressWarnings("unused")
public enum RoomSet {
    @JsonAlias("Congress Elim") CONGRESS_ELIM("http://www.example.com/1", ""),
    @JsonAlias("Congress Prelim") CONGRESS_PRELIM("http://www.example.com/2", ""),
    @JsonAlias("Debate - Early Elim") DEBATE_EARLY_ELIM("http://www.example.com/3", ""),
    @JsonAlias("Debate - Late Elim") DEBATE_LATE_ELIM("http://www.example.com/4", ""),
    @JsonAlias("Debate - Elim") DEBATE_ELIM("http://www.example.com/4", ""),
    @JsonAlias("Debate Prelim") DEBATE_PRELIM("http://www.example.com/5", ""),
    @JsonAlias({"Special - Other", "", "Special - "}) SPECIAL_OTHER("http://www.example.com/6",
        ""),
    @JsonAlias("Special - Awards") SPECIAL_AWARDS("http://www.example.com/7", ""),
    @JsonAlias("Special - EXT Prep") EXTEMP_PREP("http://www.example.com/8", ""),
    @JsonAlias("Special - Judge Call") JUDGE_CALL("http://www.example.com/9", ""),
    @JsonAlias("Special - Lounge") JUDGE_LOUNGE("http://www.example.com/10", ""),
    @JsonAlias("Special - MASS") MASS("http://www.example.com/11", ""),
    @JsonAlias("Special - Meeting") MEETING_ROOM("http://www.example.com/12", ""),
    @JsonAlias("Special - Postings Party") POSTINGS_PARTY("http://www.example.com/13", ""),
    @JsonAlias("Special - Postings Prep") POSTING_PREP("http://www.example.com/14", ""),
    @JsonAlias("Special - Registration") REGISTRATION("http://www.example.com/15", ""),
    @JsonAlias("Special - Quiet Room") QUIET_ROOM("http://www.example.com/15", ""),
    @JsonAlias("Special - Conference") CONFERENCE("http://www.example.com/15", ""),
    @JsonAlias({"Special - TAB Room", "Special - Tab Room"})
    TAB_ROOM("http://www.example.com/16", ""),
    @JsonAlias({"Special - TAB Room A", "Special - Tab Room A"})
    TAB_ROOM_A("http://www.example.com/16", ""),
    @JsonAlias({"Special - TAB Room B", "Special - Tab Room B"})
    TAB_ROOM_B("http://www.example.com/16", ""),
    @JsonAlias("Special - Work Room") WORK_ROOM("http://www.example.com/17", ""),
    @JsonAlias("Speech - Early Elim") SPEECH_EARLY_ELIM("http://www.example.com/18", ""),
    @JsonAlias("Speech - Elim") SPEECH_ELIM("http://www.example.com/18", ""),
    @JsonAlias("Speech - Late Elim") SPEECH_LATE_ELIM("http://www.example.com/19", ""),
    @JsonAlias("Speech Prelim") SPEECH_PRELIM("http://www.example.com/20", "");

    private final String title;
    private final String url;

    RoomSet(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String href() {
        return url;
    }

    public String title() {
        return title;
    }
}
