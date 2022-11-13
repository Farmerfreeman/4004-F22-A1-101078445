Feature: Part 2 - Miscellaneous Fortune Cards and Full Chest bonus

  Scenario Outline: Sorceress
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    And player rerolls <slot> to <result> with sorceress
    When player scores
    Then player score should be <expectedScore>

  Examples:
    | row | initroll | rolltwo | slot | result | card | expectedScore |
    | 77  | 'diamond, diamond, sword, monkey, coin, parrot, parrot, parrot' | 'diamond, diamond, sword, monkey, coin, skull, monkey, monkey'  | 6 | 'monkey' | 'Sorceress' | 500 |
    | 78  | 'skull, skull, skull, parrot, parrot, parrot, sword, sword' | 'skull, skull, skull, parrot, parrot, parrot, parrot, parrot'  | 1 | 'parrot' | 'Sorceress' | 1000 |
    | 78  | 'skull, parrot, parrot, parrot, parrot, monkey, monkey, monkey' | 'skull, parrot, parrot, parrot, parrot, skull, parrot, parrot'  | 1 | 'parrot' | 'Sorceress' | 2000 |

  Scenario Outline: Die on first roll
    Given player rolls <initroll>
    And player card is <card>
    When player scores
    Then player dies
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | card | expectedScore |
      | 84  | 'Skull, Skull, Skull, Monkey, Monkey, Monkey, parrot, parrot' | 'Monkey_Business' | 0 |

  Scenario Outline: Score on first roll
    Given player rolls <initroll>
    And player card is <card>
    When player scores
    Then player score should be <expectedScore>
  Examples:
    | row | initroll | card | expectedScore |
    | 82  | 'monkey, monkey, monkey, parrot, parrot, parrot, skull, coin' | 'Monkey_Business' | 1100 |
    | 97  | 'monkey, monkey, monkey, sword, sword, sword, diamond, parrot' | 'Gold' | 400 |
    | 98  | 'monkey, monkey, monkey, sword, sword, sword, coin, coin' | 'Captain' | 1800 |
    | 99  | 'monkey, monkey, monkey, sword, sword, sword, sword, diamond' | 'Gold' | 1000 |

  Scenario Outline: Score on second roll
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    When player scores
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | rolltwo | card | expectedScore |
      | 83  | 'monkey, monkey, coin, coin, sword, sword, parrot, parrot' | 'monkey, monkey, coin, coin, monkey, parrot, parrot, parrot' | 'Monkey_Business' | 1700 |

  #Treasure Chest Tests
  Scenario: Row 90
    Given player rolls 'parrot, parrot, parrot, sword, sword, diamond, diamond, coin'
    And player card is 'Treasure_Chest'
    And player places '6,7,8' in chest
    And player rolls 'parrot, parrot, parrot, parrot, parrot, diamond, diamond, coin'
    And player removes '6,7,8' from chest
    And player places '1,2,3,4,5' in chest
    And player rolls 'parrot, parrot, parrot, parrot, parrot, skull, parrot, coin'
    When player scores
    Then player score should be 1100

  Scenario: Row 94
    Given player rolls 'skull, skull, parrot, parrot, parrot, coin, coin, coin'
    And player card is 'Treasure_Chest'
    And player places '6,7,8' in chest
    And player rolls 'skull, skull, diamond, diamond, coin, coin, coin, coin'
    And player places '5' in chest
    And player rolls 'skull, skull, skull, coin, coin, coin, coin, coin'
    When player scores
    Then player score should be 600
    And player dies

  #This is the first test to leverage the actual networking code
  Scenario: Row 102
    Given the game server starts
    And player card is 'Sea_Battle_2'
    And player rolls 'monkey, monkey, monkey, monkey, sword, sword, coin, coin'