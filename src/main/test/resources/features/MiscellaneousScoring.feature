Feature: Part 2 - Miscellaneous Fortune Cards and Full Chest bonus

  //Sorceress Tests
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

  Scenario Outline: Score on second roll
    Given player rolls <initroll>
    And player card is <card>
    And player rolls <rolltwo>
    When player scores
    Then player score should be <expectedScore>

    Examples:
      | row | initroll | rolltwo | card | expectedScore |
      | 83  | 'monkey, monkey, coin, coin, sword, sword, parrot, parrot' | 'monkey, monkey, coin, coin, monkey, parrot, parrot, parrot' | 'Monkey_Business' | 1700 |