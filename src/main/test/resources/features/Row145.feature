Feature: Row145
  @Networked
  Scenario: Row145
  Given player 1 card is 'Captain'
  And player 1 rolls 'skull, skull, skull, monkey, monkey, monkey, monkey, monkey'


  And player 2 card is 'Captain'
  And player 2 rolls 'sword, sword, sword, sword, sword, sword, sword, skull'
  And player 2 scores

  And player 3 card is 'Skull_2'
  And player 3 rolls 'skull, sword, sword, sword, sword, sword, sword, sword'


  And set input '1'
  And WAIT P 2 TURN



  Given player 1 card is 'Captain'
  And player 1 rolls 'sword, sword, sword, sword, sword, sword, sword, sword'
  And player 1 scores



  When WAIT GAME END

  Then game ends
  And player 1 won
  And player 1 score should be 9000
  And player 2 score should be 4000
  And player 3 score should be 0