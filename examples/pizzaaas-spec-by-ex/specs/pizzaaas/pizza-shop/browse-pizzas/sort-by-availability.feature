# Order: 24
# Milestone: R4

Feature: Sort by Availability
  I can sort Pizzas by Availability

  Scenario Outline: Sort by Availability
    Given a dummy gherkin scenarioo
    When I browse to Pizzas
    And I select country Switzerland for my order
    Then I can see <pizza> in row <row>

    Examples:
      | pizza       | row |
      | Margarita   | 1   |
      | Funghi      | 2   |
