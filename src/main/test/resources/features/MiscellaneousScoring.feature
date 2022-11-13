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