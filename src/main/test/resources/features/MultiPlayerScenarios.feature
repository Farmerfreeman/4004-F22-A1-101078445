Feature: Part 3 - Multi-player Scenarios

  @Networked
  Scenario: row132
    Given player 1 card is 'Captain'
    And player 1 rolls 'sword, sword, sword, sword, sword, sword, sword, skull'
    And player 1 scores


    And player 2 card is 'Skull_1'
    And player 2 rolls 'sword, sword, sword, sword, sword, sword, sword, skull'
    And player 2 scores


    And player 3 card is 'Gold'
    And player 3 rolls 'skull, skull, skull, monkey, monkey, monkey, monkey, monkey'


    And set input '1 1'

    When WAIT GAME END

    Then game ends
