package org.ncfl.specs;

import com.fasterxml.jackson.annotation.JsonAlias;
import j2html.tags.DomContent;
import jakarta.annotation.Nonnull;

import static j2html.TagCreator.li;
import static j2html.TagCreator.ul;

@SuppressWarnings("java:S1192")
public enum RoomSet {

    @JsonAlias("Special - Lounge") SPECIAL_LOUNGE(null, "Special - Lounge", ul(li("???"))),
    @JsonAlias({"Special Extemp Prep", "Special - Extemp Prep"}) SPECIAL_EXT_PREP(null,
        "Special - Extemp Prep",
        ul(
            li("Head Table for 12"),
            li("Microphone"),
            li("Schoolroom seating for 250")
        )),
    @JsonAlias({"Special - Office  (NCFL/Local CFl Office)", "Special - Office"}) SPECIAL_OFFICE(null,
        "Special - Office",
        ul(
            li("Schoolroom tables around perimeter of room - as many as you can fit"),
            li("8 chairs stacked in corner")
        )
    ),
     @JsonAlias("Special - Conference (Equity / Protest)") SPECIAL_EQUITY_PROTEST(null,
         "Special - Equity / Protest",
         ul(
            li("Conference table for 8"),
            li("2 schoolroom tables against the wall (if they fit)")
        )
    ),
    @JsonAlias("Special - Meeting (Judge Standby)") SPECIAL_JUDGE_STBY(null,
        "Special - Meeting (Judge Standby)",
        ul(
            li("Head table for 4"),
            li("Microphone"),
            li("Theater seating to capacity"),
            li("Two schoolroom tables with chairs at back of room, behind theater seating")
        )
    ),
    @JsonAlias({"Special - Speech Workroom (Speech Tab & Registration Room)", "Special - Speech Workroom"}) SPECIAL_WORKROOM_1(null,
        "Special - Speech Workroom",
        ul(
            li("Schoolroom tables around perimeter of room - as many as you can fit"),
            li("16 chairs stacked in corner"),
            li("Eight 60” rounds with 6 chairs each")
        )
    ),
    @JsonAlias({"Special - Debate Workroom (Cong, PF, LD, Pol Tab Rooms)", "Special - Debate Workroom"}) SPECIAL_WORKROOM_2(null,
        "Special - Debate Workroom",
        ul(
            li("Schoolroom tables around perimeter of room - as many as you can fit"),
            li("8 chairs stacked in corner"),
            li("Conference table for 12 in center")
        )
    ),
    @JsonAlias("Special - Postings Party") SPECIAL_POSTINGS_PARTY(null,
        "Special - Postings Party",
        ul(li("Set up TBA"))),
    @JsonAlias("Special - Postings Prep") SPECIAL_POSTINGS_PREP(null, "Special - Postings Prep", ul(li("Set up TBA"))),
    @JsonAlias("Special - Meeting") SPECIAL_MEETING(null, "Special - Meeting", ul(li("Set up TBA"))),
    @JsonAlias("Special - MASS") SPECIAL_MASS(null, "Special - MASS", ul(li("Set up TBA"))),
    @SuppressWarnings("SpellCheckingInspection") @JsonAlias({"Special - Conference", "Special - Coference"}) SPECIAL_CONFERENCE(null, "Special - Conference", ul(li("Set up TBA"))),

    @JsonAlias("Special - Awards") SPECIAL_AWARDS(null, "Special - Awards",
        ul(
            li("Stage platform  approximately 36’w x 16’’d"),
            li("Podium with microphone downstage right"),
            li("Two sets of stairs with handrail on each side of stage"),
            li("ADA compliant wheelchair ramp on at least one side, preferably both"),
            li("8 6’x3’ tables  draped in black toward back of stage for trophies, measures 20 feet across"),
            li("3 chairs stage right behind podium"),
            li("Two schoolroom tables off stage left draped in black for unclaimed trophies"),
            li("Theater seating to capacity")
        )
    ),
    @JsonAlias("Special - Quiet Room") SPECIAL_QUIET_ROOM(null, "Special - Quiet Room",
        ul(li("Lowboy tables with 2 chairs at each."))),
    @JsonAlias("Debate Prelim") DEBATE_PRELIM(null, "Debate Prelim", ul(
        li("Two head tables, side by side with a space between them. Two chairs at each"),
        li("Three schoolroom tables with one chair each facing head table."),
        li("One row of theater seating behind judges table")
    )),
    @JsonAlias("Debate Prelim - Conference") DEBATE_PRELIM_CONFERENCE(null, "Debate Prelim - Conference", ul(
        li("Two head tables, side by side with a space between them. Two chairs at each"),
        li("Three schoolroom tables with one chair each facing head table."),
        li("One row of theater seating behind judges table")
    )),
    @JsonAlias("Debate Prelim - Cubicle") DEBATE_PRELIM_CUBICLE(null,
        "Debate Prelim - Cubicle",
        ul(
            li("Two head tables, side by side with a space between them. Two chairs at each"),
            li("Three schoolroom tables with one chair each facing head table."),
            li("One row of theater seating behind judges table")
        )
    ),
    @JsonAlias("Debate - Elim") DEBATE_ELIM(null,
        "Debate - Elim",
        ul(
            li("Two head tables, side by side with a space between them. Two chairs at each"),
            li("Five schoolroom tables with one chair each facing head table. If 5 tables do not fit, please put three tables with 5 chairs."),
            li("Theater seating to capacity behind judges table")
        )
    ),
    @JsonAlias({"Congress Prelim", "Student Congress Prelim"}) CONGRESS_PRELIM(null,
        "Congress Prelim",
        ul(
            li("Head Table for two plus podium"),
            li("US Flag in corner of room"),
            li("Schoolroom seating for 18, plus two additional schoolroom tables in the back row, one at each corner of the room.")
        )
    ),
    @JsonAlias("Congress Elim") CONGRESS_ELIM(null,
        "Congress Elim",
        ul(
            li("Head Table for two plus podium"),
            li("US Flag in corner of room"),
            li("Schoolroom seating for 24, plus two additional schoolroom tables in the back row, one at each corner of the room."),
            li("Theater seating to capacity behind schoolroom seating")
        )
    ),
    @JsonAlias("Speech - Prelim") SPEECH_PRELIM(null, "Speech - Prelim",
        ul(
            li("NO head table. Please leave a performance space at the front of the room where speakers will stand to present."),
            li("Three schoolroom tables with one chair each facing the performance space"),
            li("One row of theater seating behind judges table")
        )
    ),
    @JsonAlias("Speech Prelim - Cubicle") SPEECH_PRELIM_CUBICLE(null,
        "Speech Prelim - Cubicle",
        ul(
            li("NO head table. Please leave a performance space at the front of the room where speakers will stand to present."),
            li("Three schoolroom tables with one chair each facing the performance space"),
            li("One row of theater seating behind judges table")
        )
    ),
    @JsonAlias("Speech Prelim - Conference") SPEECH_PRELIM_CONFERENCE(null,
        "Speech Prelim - Conference",
        ul(
            li("NO head table. Please leave a performance space at the front of the room where speakers will stand to present."),
            li("Three schoolroom tables with one chair each facing the performance space"),
            li("One row of theater seating behind judges table")
        )
    ),
    @JsonAlias("Speech - Elim") SPEECH_ELIM(null,
        "Speech - Elim",
        ul(
            li("NO head table. Please leave a performance space at the front of the room where speakers will stand to present."),
            li("Three schoolroom tables with one chair each facing the performance space"),
            li("Theater seating to capacity behind judges table")
        )
    ),
    SPECIAL_OTHER(null, null, ul(li("???")));

    private final String title;
    private final String url;

    @Nonnull
    private final DomContent description;

    RoomSet(String url, String title, @Nonnull DomContent description) {
        this.url = url;
        this.title = title;
        this.description = description;
    }

    public String href() {
        return url;
    }

    public String title() {
        return title;
    }

    public DomContent description() {
        return description;
    }

    @Override
    public String toString() {
        return title;
    }
}
