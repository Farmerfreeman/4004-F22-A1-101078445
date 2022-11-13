Feature: Single PLayer Scoring

  Scenario Outline: Die on first roll
    Given player rolls <initroll>
    And player card is <card>
    When player scores
    Then player dies
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | card | expectedScore |
      | 45  | 'Skull, Skull, Skull, Sword, Sword, Sword, Sword, Sword' | 'Gold' | 0 |
