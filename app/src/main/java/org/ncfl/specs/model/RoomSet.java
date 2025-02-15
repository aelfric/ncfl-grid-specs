package org.ncfl.specs.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import j2html.tags.specialized.UlTag;
import jakarta.annotation.Nonnull;

import java.util.function.Supplier;

import static j2html.TagCreator.li;
import static j2html.TagCreator.ul;

// TODO in the future should be able to recognize "existing set" when the room setup does not
//      change between usages

@SuppressWarnings("java:S1192")
public enum RoomSet {
    @JsonAlias({"Special - Extemp Prep Saturday"})
    SPECIAL_EXT_PREP_SATURDAY(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c3709c658e_0_994",
        "Special - Extemp Prep Saturday",
        () -> ul(
            li("Head table - 12 schoolroom tables, each with one chair"),
            li("Microphone"),
            li("Schoolroom seating for 250")
        ),
        false),
    @JsonAlias({"Special - Extemp Prep Sunday"})
    SPECIAL_EXT_PREP_SUNDAY(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c9c06c9732_0_1001",
        "Special - Extemp Prep Sunday",
        () -> ul(
            li("Head table - 12 schoolroom tables, each with one chair"),
            li("Microphone"),
            li("Schoolroom seating for 48")
        ),
        false),
    @JsonAlias({"Special - Office  (NCFL/Local CFl Office)", "Special - Office"}) SPECIAL_OFFICE(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c3709c658e_0_799",
        "Special - Office",
        () -> ul(
            li("Schoolroom tables around perimeter of room - as many as you can fit"),
            li("8 chairs stacked in corner"),
            li("Key access needed")
        ),
        false
    ),
    @JsonAlias({"Special - Conference (Equity / Protest)", "Special - Conference"}) SPECIAL_EQUITY_PROTEST(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c3709c658e_0_832",
        "Special - Conference",
        () -> ul(
            li("Conference table for 12 in center"),
            li("Two additional tables (with no chairs) against the wall for printer/supplies.")
        ),
        false
    ),
    @JsonAlias({"Special - Speech Workroom (Speech Tab & Registration Room)", "Special - Speech Workroom"}) SPECIAL_SPEECH_WORKROOM(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g125e6cf3e8a_0_0",
        "Special - Speech Workroom",
        () -> ul(
            li("Schoolroom tables around perimeter of room - as many as you can fit"),
            li("16 chairs stacked in corner"),
            li("Eight 60” rounds with 6 chairs each")
        ),
        false
    ),
    @JsonAlias({"Special - Debate Workroom (Cong, PF, LD, Pol Tab Rooms)", "Special - Debate Workroom"}) SPECIAL_DEBATE_WORKROOM(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g22e25dd92da_2_251",
        "Special - Debate Workroom",
        () -> ul(
            li("Schoolroom tables around perimeter of room - as many as you can fit"),
            li("8 chairs stacked in corner"),
            li("Conference table for 12 in center")
        ),
        false
    ),
    @JsonAlias("Special - Postings Party") SPECIAL_POSTINGS_PARTY(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c3709c658e_0_166",
        "Special - Postings Party",
        () -> ul(li("Setup TBD: we will likely need a platform stage for a DJ, a dance floor, " +
            "and some lowboys")),
        false),
    @JsonAlias("Special - Postings Prep") SPECIAL_POSTINGS_PREP(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c5d9ec8741_0_324",
        "Special - Postings Prep",
        () -> ul(
            li("Table for 12 down center of the room"),
            li("Additional tables against the walls - as many as possible"),
            li("Stack of 10 additional chairs in corner")
        ),
        false),
    @JsonAlias({"Special - Meeting",
        "Special - Meeting Room",
        "Special - Meeting (Judge Standby)"}) SPECIAL_MEETING(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g22e25dd92da_2_146",
        "Special - Meeting",
        () -> ul(
            li("Head table for 4"),
            li("Microphone"),
            li("Theater seating to capacity"),
            li("Two schoolroom tables with chairs at back of room, behind theater seating")
        ),
        false),
    @JsonAlias("Special - MASS") SPECIAL_MASS(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g26e8965b45c_0_2",
        "Special - MASS",
        () -> ul(
            li("Tables set up to use as altar - Preferably two 36”x72” tables, draped in " +
                "white."),
            li("We will need 4 “nice” chairs for clergy to sit at the altar. Do you have any upright " +
                "wooden chairs (preferably with arms) that would be appropriate?"),
            li("Podium with microphone"),
            li("Theater seating to capacity")
        ),
        false),
    @JsonAlias("Special - Awards") SPECIAL_AWARDS(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g23d9ba7c43f_2_0",
        "Special - Awards",
        () -> ul(
            li("Stage platform - minimally 68’ wide by 16’ deep"),
            li("Podium with microphone downstage right"),
            li("Stairs with handrail on each side of stage"),
            li("ADA compliant wheelchair ramp on at least one side, preferably both"),
            li("10 6’x3’ tables  draped in black toward back of stage for trophies, measures 20 feet " +
                "across"),
            li("3 chairs stage right behind podium"),
            li("Two schoolroom tables off stage left draped in black for unclaimed trophies"),
            li("Theater seating to absolute maximum")
        ),
        false
    ),
    @JsonAlias("Special - Quiet Room") SPECIAL_QUIET_ROOM(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c3709c658e_0_160",
        "Special - Quiet Room",
        () -> ul(li("Lowboy tables with 2 chairs at each.")),
        false),
    @JsonAlias("Debate Prelim - Conference") DEBATE_PRELIM_CONFERENCE(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.p4",
        "Debate Prelim - Conference",
        () -> ul(
            li("Two head tables, side by side with a space between them. Two chairs at each table."),
            li("Three schoolroom tables with one chair each facing the head table for judges’ table."),
            li("One row of theater seating behind judges table.")
        ),
        true),
    @JsonAlias("Debate Prelim - Cubicle") DEBATE_PRELIM_CUBICLE(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c9c06c9732_0_111",
        "Debate Prelim - Cubicle",
        () -> ul(
            li("Two head tables, side by side with a space between them. Two chairs at each"),
            li("Three schoolroom tables with one chair each facing head table."),
            li("No additional audience seating needed in cubicles.")
        ),
        true
    ),
    @JsonAlias("Debate - Elim") DEBATE_ELIM(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c9c06c9732_0_151",
        "Debate - Elim",
        () -> ul(
            li("Two head tables, side by side with a space between them. Two chairs at each table."),
            li("Five schoolroom tables with one chair each facing the head table. If 5 tables do not fit," +
                "please put three tables with 5 chairs"),
            li("Theater seating to capacity behind judges table")
        ),
        false
    ),
    @JsonAlias({"Congress Prelim", "Student Congress Prelim", "Student Cognress Prelim"}) CONGRESS_PRELIM(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g23e955c9bfa_0_0",
        "Congress Prelim",
        () -> ul(
            li("Head Table for two plus podium to the side"),
            li("US Flag in corner of room"),
            li("Schoolroom seating for 18, plus two additional schoolroom tables in the back row, one at " +
                "each corner of the room."),
            li("Please note, slide is a suggested configuration. Please let me know if another " +
                "configuration needs to be used to fit the room.")
        ),
        true
    ),
    @JsonAlias("Congress Elim") CONGRESS_ELIM(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c9c06c9732_0_707",
        "Congress Elim",
        () -> ul(
            li("Head Table for two plus podium to the side"),
            li("US Flag in corner of room"),
            li("Schoolroom seating for 24, plus two additional schoolroom tables in the back row, one at " +
                "each corner of the room."),
            li("Theater seating to capacity behind schoolroom seating")
        ),
        false
    ),
    @JsonAlias("Speech Prelim - Cubicle") SPEECH_PRELIM_CUBICLE(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c9c06c9732_0_333",
        "Speech Prelim - Cubicle",
        () -> ul(
            li("NO head table. Please leave a performance space at the front of the room " +
                "where speakers " +
                "will stand to present."),
            li("Three schoolroom tables with one chair each facing the performance space"),
            li("No additional audience seating needed in cubicles")
        ),
        true
    ),
    @JsonAlias("Speech Prelim - Conference") SPEECH_PRELIM_CONFERENCE(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c9c06c9732_0_292",
        "Speech Prelim - Conference",
        () -> ul(
            li("NO head table. Please leave a performance space at the front of the room where speakers " +
                "will stand to present."),
            li("Three schoolroom tables with one chair each facing the performance space"),
            li("One row of theater seating behind judges table")
        ),
        true
    ),
    @JsonAlias("Speech - Elim") SPEECH_ELIM(
        "https://docs.google.com/presentation/d/1BeJvExjJ97S0ZYhR4zYZj7mNEjeob8H1Z0ptErcsF3Y/edit#slide=id.g2c9c06c9732_0_362",
        "Speech - Elim",
        () -> ul(
            li("NO head table. Please leave a performance space at the front of the room where speakers " +
                "will stand to present."),
            li("Five schoolroom tables with one chair each facing the performance space. If 5 tables do " +
                "not fit, please put three tables with 5 chairs."),
            li("Theater seating to capacity behind judges table.")
        ),
        false
    ),
    @JsonAlias("Existing Set")
    EXISTING_SET("#", "Existing Set", () -> ul(li("Leave room as-is")), false),
    SPECIAL_OTHER("#", null, () -> ul(li("???")), false);

    private final String title;
    private final String url;

    private final Supplier<UlTag> description;

    private final boolean prelim;

    RoomSet(String url, String title, @Nonnull Supplier<UlTag> description, boolean prelim) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.prelim = prelim;
    }

    public String href() {
        return url;
    }

    public String title() {
        return title;
    }

    public UlTag description() {
        return description.get();
    }

    @Override
    public String toString() {
        return title;
    }

    public boolean isPrelim() {
        return prelim;
    }
}
