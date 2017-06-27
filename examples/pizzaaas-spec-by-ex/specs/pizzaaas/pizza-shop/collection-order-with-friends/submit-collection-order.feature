# Order: 3
# Milestone: R1

Feature: Submit Collection Order

  Scenario:Submit a Collection Order
    Given A collection order
    And with added pizzas by different users
    When I browse to that order in my account
    And I select to order this order
    Then I am navigated to the usual checkout of the shopping basket
    And I can modify my order there
    And continue the chekcout for final ordering
