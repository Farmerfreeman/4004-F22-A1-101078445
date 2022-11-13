Feature: Single PLayer Scoring

  Scenario Outline: Die on first roll
    Given player rolls <initroll>
    Given player card is <card>
    When player scores
    Then their score should be <expectedScore>

    Examples:
      | row | initroll | card | expectedScore |
      | 45  | 'Skull, Skull, Skull, Sword, Sword, Sword, Sword, Sword' | 'Gold' | 0 |
