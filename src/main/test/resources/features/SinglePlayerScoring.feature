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

  Scenario Outline: Die on second roll
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    When player scores
    Then player dies
    Then player score should be <expectedScore>

  Examples:
    | row | initroll | rolltwo | card | expectedScore |
    | 46  | 'skull, parrot, parrot, parrot, parrot, sword, sword, sword' | 'skull, parrot, parrot, parrot, parrot, skull, skull, sword'  | 'Gold' | 0 |
    | 47  | 'skull, skull, parrot, parrot, parrot, parrot, sword, sword' | 'skull, skull, parrot, parrot, parrot, parrot, skull, sword'  | 'Gold' | 0 |

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
      | 49  | 'skull, parrot, parrot, parrot, parrot, sword, sword, sword' | 'skull, parrot, parrot, parrot, parrot, skull, monkey, monkey' | 'skull, parrot, parrot, parrot, parrot, skull, skull, monkey'  | 'Gold' | 0 |

  Scenario Outline: Score on first roll
    Given player rolls <initroll>
    And player card is <card>
    When player scores
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | card | expectedScore |
      | 52  | 'monkey, monkey, parrot, parrot, diamond, diamond, coin, coin' | 'Captain' | 800 |

  Scenario Outline: Score on second roll
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    When player scores
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | rolltwo | card | expectedScore |
      | 53  | 'monkey, monkey, skull, skull, sword, sword, parrot, parrot' | 'monkey, monkey, skull, skull, sword, sword, sword, monkey'  | 'Gold' | 300 |

  Scenario Outline: Score on third roll
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    And player rolls <rollthree>
    When player scores
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | rolltwo | rollthree | card | expectedScore |
      | 51  | 'skull, parrot, parrot, sword, sword, sword, coin, coin' | 'skull, coin, coin, sword, sword, sword, coin, coin' | 'skull, coin, coin, coin, coin, coin, coin, coin'  | 'Gold' | 4800 |