# Milestone: R2

Feature: Price Calculation
  As owner of this business
  I want the price calculation of the pizzas
  always be done correctly and depending on current customization

  Scenario: With Default Ingredient Prices
    Given Simple default pizza prices
    When I calculate the price for pizza <pizza>
    Then the resulting price should be <price>
