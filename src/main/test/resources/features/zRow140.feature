Feature: Row140
  @Networked
  Scenario: row140
    Given player 1 card is 'Captain'
    And player 1 rolls 'sword, sword, sword, sword, sword, sword, sword, skull'
    And player 1 scores

    And set input '1'

    And player 2 card is 'Gold'
    And player 2 rolls 'skull, skull, skull, monkey, monkey, monkey, monkey, monkey'
    And player 2 scores

    And player 3 card is 'Captain'
    And player 3 rolls 'skull, skull, skull, skull, skull, skull, parrot, parrot'
    And player 3 scores

    And WAIT P 2 TURN

    And set input 'N'

    And WAIT P 3 TURN

    And set input '1'

    And player 1 card is 'Gold'
    And player 1 rolls 'monkey, monkey, monkey, monkey, parrot, parrot, parrot, parrot'
    And player 1 scores


    And player 2 card is 'Captain'
    And player 2 rolls 'skull, skull, skull, monkey, monkey, monkey, monkey, monkey'

    And player 3 card is 'Skull_1'
    And player 3 rolls 'skull, skull, monkey, monkey, monkey, monkey, monkey, monkey'

    When WAIT GAME END


    Then game ends
    And player 1 won
    And player 1 score should be 3800
    And player 2 score should be 0
    And player 3 score should be 0