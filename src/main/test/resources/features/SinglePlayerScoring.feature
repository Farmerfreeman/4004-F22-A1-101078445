Feature: Part 1 - Single PLayer Scoring

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
      | 54  | 'monkey, monkey, monkey, sword, sword, sword, skull, skull' | 'Gold' | 300 |
      | 55  | 'diamond, diamond, diamond, skull, skull, monkey, sword, parrot' | 'Gold' | 500 |
      | 56  | 'coin, coin, coin, coin, skull, skull, sword, sword' | 'Diamond' | 700          |
      | 57  | 'coin, coin, coin, coin, skull, skull, sword, sword' | 'Diamond' | 700 |
      | 62  | 'monkey, monkey, monkey, monkey, monkey, monkey, skull, skull' | 'Gold' | 1100 |
      | 63  | 'parrot, parrot, parrot, parrot, parrot, parrot, parrot, skull' | 'Gold' | 2100 |
      | 64  | 'coin, coin, coin, coin, coin, coin, coin, coin' | 'Gold' | 5400 |
      | 65  | 'coin, coin, coin, coin, coin, coin, coin, coin' | 'Diamond' | 5400 |
      | 66  | 'sword, sword, sword, sword, sword, sword, sword, sword' | 'Captain' | 9000 |
      | 72  | 'monkey, monkey, monkey, monkey, coin, coin, skull, skull' | 'Gold' | 600 |


  Scenario Outline: Score on second roll
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    When player scores
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | rolltwo | card | expectedScore |
      | 53  | 'monkey, monkey, skull, skull, sword, sword, parrot, parrot' | 'monkey, monkey, skull, skull, sword, sword, sword, monkey'  | 'Gold' | 300 |
      | 58  | 'skull, coin, coin, parrot, parrot, sword, sword, sword' | 'skull, coin, coin, coin, sword, sword, sword, sword'  | 'Gold' | 800 |
      | 59  | 'skull, coin, coin, parrot, parrot, sword, sword, sword' | 'skull, coin, coin, coin, sword, sword, sword, sword'  | 'Captain' | 1200 |
      | 67  | 'monkey, monkey, monkey, monkey, monkey, monkey, sword, sword' | 'monkey, monkey, monkey, monkey, monkey, monkey, monkey, monkey' | 'Gold' | 4600 |
      | 68  | 'monkey, monkey, skull, skull, parrot, parrot, sword, sword' | 'monkey, monkey, skull, skull, diamond, diamond, sword, sword' | 'Diamond' | 400 |
      | 69  | 'monkey, monkey, skull, skull, diamond, parrot, sword, sword' | 'diamond, diamond, skull, skull, diamond, parrot, sword, sword' | 'Gold' | 500 |
      | 70  | 'skull, coin, coin, monkey, parrot, sword, sword, sword' | 'skull, coin, coin, monkey, parrot, coin, monkey, parrot' | 'Gold' | 600 |
      | 71  | 'skull, coin, coin, monkey, parrot, sword, sword, sword' | 'skull, coin, coin, monkey, parrot, coin, monkey, parrot' | 'Diamond' | 500 |

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
      | 61  | 'skull, parrot, parrot, monkey, monkey, sword, sword, sword' | 'skull, parrot, parrot, skull, sword, sword, sword, sword'  | 'skull, sword, monkey, skull, sword, sword, sword, sword'  | 'Gold' | 600 |