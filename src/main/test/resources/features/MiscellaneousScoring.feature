Feature: Part 2 - Miscellaneous Fortune Cards and Full Chest bonus

  Scenario Outline: Die on first roll
    Given player rolls <initroll>
    And player card is <card>
    When player scores
    Then player dies
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | card | expectedScore |


  Scenario Outline: Die on second roll
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    When player scores
    Then player dies
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | rolltwo | card | expectedScore |


  Scenario Outline: Die on third roll
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    And player rolls <rollthree>
    When player scores
    Then player dies
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | rolltwo | rollthree | card | expectedScore |


  Scenario Outline: Score on first roll
    Given player rolls <initroll>
    And player card is <card>
    When player scores
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | card | expectedScore |



  Scenario Outline: Score on second roll
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    When player scores
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | rolltwo | card | expectedScore |


  Scenario Outline: Score on third roll
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    And player rolls <rollthree>
    When player scores
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | rolltwo | rollthree | card | expectedScore |
