Feature: Part 2 - Miscellaneous Fortune Cards and Full Chest bonus

  Scenario Outline: Row77,78,79
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
    | 79  | 'skull, parrot, parrot, parrot, parrot, monkey, monkey, monkey' | 'skull, parrot, parrot, parrot, parrot, skull, parrot, parrot'  | 1 | 'parrot' | 'Sorceress' | 2000 |

  Scenario Outline: Row84
    Given player rolls <initroll>
    And player card is <card>
    When player scores
    Then player dies
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | card | expectedScore |
      | 84  | 'Skull, Skull, Skull, Monkey, Monkey, Monkey, parrot, parrot' | 'Monkey_Business' | 0 |

  Scenario Outline: Row82,97,98,99
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

  Scenario Outline: Row83
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    When player scores
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | rolltwo | card | expectedScore |
      | 83  | 'monkey, monkey, coin, coin, sword, sword, parrot, parrot' | 'monkey, monkey, coin, coin, monkey, parrot, parrot, parrot' | 'Monkey_Business' | 1700 |

  #Treasure Chest Tests
  Scenario: Row90
    Given player rolls 'parrot, parrot, parrot, sword, sword, diamond, diamond, coin'
    And player card is 'Treasure_Chest'
    And player places '6,7,8' in chest
    And player rolls 'parrot, parrot, parrot, parrot, parrot, diamond, diamond, coin'
    And player removes '6,7,8' from chest
    And player places '1,2,3,4,5' in chest
    And player rolls 'parrot, parrot, parrot, parrot, parrot, skull, parrot, coin'
    When player scores
    Then player score should be 1100

  Scenario: Row94
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
  @Networked_sp
  Scenario: Row102

    Given player 1 card is 'Sea_Battle_2'

    Given player 1 rolls 'monkey, monkey, monkey, monkey, sword, parrot, parrot, coin'
    And set input 'Y 6,7'

    Given player 1 rolls 'monkey, monkey, monkey, monkey, sword, sword, coin, coin'


    When turn 1 ends

    Then player 1 score should be 1200

  @Networked_sp
  Scenario: Row106
    Given player 1 card is 'Skull_2'
    And player 1 rolls 'Skull, sword, sword, sword, sword, sword, sword, sword'

    When turn 1 ends

    Then player 1 dies
    Then player 1 score should be 0




