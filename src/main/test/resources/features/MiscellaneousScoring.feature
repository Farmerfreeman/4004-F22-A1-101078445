Feature: Part 2 - Miscellaneous Fortune Cards and Full Chest bonus

  #Scenario Outline: Player rerolls using the sorceress
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

  #Scenario Outline: Player dies on the first roll
  Scenario Outline: Row84,106
    Given player rolls <initroll>
    And player card is <card>
    When player scores
    Then player dies
    And player score should be <expectedScore>

    Examples:
      | row | initroll | card | expectedScore |
      | 84  | 'Skull, Skull, Skull, Monkey, Monkey, Monkey, parrot, parrot' | 'Monkey_Business' | 0 |
      | 106  | 'Skull, sword, sword, sword, sword, sword, sword, sword' | 'Skull_2' | 0 |
      | 107  | 'Skull, Skull, sword, sword, sword, sword, sword, sword' | 'Skull_1' | 0 |

  #Scenario Outline: Player scores on the first roll
  Scenario Outline: Row82,97,98,99,103
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
    | 103  | 'monkey, monkey, parrot, coin, coin, diamond, diamond, diamond' | 'Monkey_Business' | 1200 |

  #Scenario Outline: Player scores on the second roll
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

  #All tests below are single player networked tests. These tests leverage the actual networking code via threads,
  #however the game is ended after a single turn and the score is asserted against.
  #Only a single player thread is created in these tests, and the game server is modified slightly to allow a
  #single player to play.
  @Networked_sp
  Scenario: Row102

    Given player 1 card is 'Sea_Battle_2'

    And player 1 rolls 'monkey, monkey, monkey, monkey, sword, parrot, parrot, coin'
    And set input 'Y 6,7'

    And player 1 rolls 'monkey, monkey, monkey, monkey, sword, sword, coin, coin'


    When turn 1 ends

    Then player 1 score should be 1200


  @Networked_sp
  Scenario: Row109
    Given player 1 card is 'Skull_2'

    And player 1 rolls 'skull, skull, parrot, parrot, parrot, monkey, monkey, monkey'
    And set input 'Y 3,4,5'

    And player 1 rolls 'skull, skull, skull, skull, sword, monkey, monkey, monkey'
    And set input 'Y 5,6,7,8'

    And player 1 rolls 'skull, skull, skull, skull, sword, skull, skull, skull'
    And set input 'N'

    When turn 1 ends

    #Player score without player number checks player score for the turn(in this case deduction)
    Then player score should be -900

    #Player {int} score checks total_score. A representation of the players actual current score
    And player 1 score should be 0
    And player 2 score should be 0
    And player 3 score should be 0

  @Networked_sp
  Scenario: Row110
    Given player 1 card is 'Captain'

    And player 1 rolls 'skull, skull, skull, skull, skull, monkey, monkey, monkey'
    And set input 'Y 6,7,8'

    And player 1 rolls 'skull, skull, skull, skull, skull, skull, skull, coin'
    And set input 'N'

    When turn 1 ends

    Then player score should be -1400

    And player 1 score should be 0
    And player 2 score should be 0
    And player 3 score should be 0

  @Networked_sp
  Scenario: Row111
    Given player 1 card is 'Skull_2'

    And player 1 rolls 'skull, skull, skull, sword, sword, sword, sword, sword'
    And set input 'Y 4,5,6,7,8'

    And player 1 rolls 'skull, skull, skull, coin, coin, coin, coin, coin'


    When turn 1 ends

    Then player score should be -500

    And player 1 score should be 0
    And player 2 score should be 0
    And player 3 score should be 0

  @Networked_sp
  Scenario: Row114
    Given player 1 card is 'Sea_Battle_2'

    And player 1 rolls 'skull, skull, skull, sword, monkey, monkey, monkey, monkey'

    When turn 1 ends

    Then player score should be -300
    And player dies
    #checking for no negative scores
    And player 1 score should be 0

  @Networked_sp
  Scenario: Row115
    Given player 1 card is 'Sea_Battle_3'

    And player 1 rolls 'sword, sword, skull, skull, parrot, parrot, parrot, parrot'
    And set input 'Y 5,6,7,8'

    And player 1 rolls 'sword, sword, skull, skull, skull, skull, skull, skull'

    When turn 1 ends

    Then player score should be -500
    And player dies
    #checking for no negative scores
    And player 1 score should be 0

  @Networked_sp
  Scenario: Row116
    Given player 1 card is 'Sea_Battle_4'

    And player 1 rolls 'monkey, monkey, skull, skull, skull, sword, sword, sword'

    When turn 1 ends

    Then player score should be -1000
    And player dies
    #checking for no negative scores
    And player 1 score should be 0

  @Networked_sp
  Scenario: Row117
    Given player 1 card is 'Sea_Battle_2'

    And player 1 rolls 'monkey, monkey, monkey, sword, sword, coin, parrot, parrot'

    When turn 1 ends

    Then player score should be 500

  @Networked_sp
  Scenario: Row119
    Given player 1 card is 'Sea_Battle_2'

    And player 1 rolls 'monkey, monkey, monkey, monkey, sword, skull, parrot, parrot'
    And set input 'Y 7,8'

    And player 1 rolls 'monkey, monkey, monkey, monkey, sword, skull, sword, skull'

    When turn 1 ends

    Then player score should be 500

  @Networked_sp
  Scenario: Row120
    Given player 1 card is 'Sea_Battle_3'

    And player 1 rolls 'monkey, monkey, monkey, sword, sword, sword, sword, skull'

    When turn 1 ends

    Then player score should be 800

  @Networked_sp
  Scenario: Row122
    Given player 1 card is 'Sea_Battle_3'

    And player 1 rolls 'monkey, monkey, monkey, monkey, sword, sword, skull, skull'
    And set input 'Y 1,2,3,4'

    And player 1 rolls 'skull, skull, sword, sword, sword, sword, skull, skull'

    When turn 1 ends

    Then player score should be -500
    And player dies
    #checking for no negative scores
    And player 1 score should be 0

  @Networked_sp
  Scenario: Row123
    Given player 1 card is 'Sea_Battle_4'

    And player 1 rolls 'monkey, monkey, monkey, sword, sword, sword, sword, skull'

    When turn 1 ends

    Then player score should be 1300

  @Networked_sp
  @Fails
  Scenario: Row126
    Given player 1 card is 'Sea_Battle_4'

    And player 1 rolls 'monkey, monkey, monkey, sword, skull, diamond, parrot, parrot'
    And set input 'Y 7,8'

    And player 1 rolls 'monkey, monkey, monkey, sword, skull, diamond, sword, sword'
    And set input 'Y 1,2,3'

    And player 1 rolls 'sword, parrot, parrot, sword, skull, diamond, sword, sword'

    When turn 1 ends

    Then player score should be 1300
