# Order: 0
# Milestone: R1

Feature: See Pizzas
  I can see the list of Pizzas
  with name and ingredients and a picture

  Scenario: See default pizzas
    Given our standard pizza catalogue
    When I browse to Pizzas
    And I select country Switzerland for my order
    Then I can see at least following pizzas
    | pizza       | ingredients |
    | Margarita   | Tomaten, Mozzarella, Oregano |
    | Funghi      | Tomaten, Mozzarella, Champignons, Oregano |
    And Each of them has a picture
