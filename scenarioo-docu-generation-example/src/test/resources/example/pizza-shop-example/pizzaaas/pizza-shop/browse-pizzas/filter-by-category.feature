# Order: 3
# Milestone: R2

Feature: Filter by Category
  In order to find pizzas quickly
  I can filter them by category

  Scenario Outline: Filter by category
    Given our default pizzas
    When I filter the list of Pizzas for Category <category>
    Then I see <pizzas>
    But I dont see <filtered-pizzas>

    Examples:
      | category            | pizzas | filteredPizzas
      | Standard            | Margarita, Funghi, Prosciutto  |  Diavolo, Michelangelo, Hawai  |
      | Spicy               | Diavolo, Vulcano, Salame Piccante  |  Margarita, Funghi, Prosciutto |
      | Exotic              | Hawai, Frutti die Mare  |  Margarita, Prosciutto, Diavolo |
      | Creative            | Michelangelo |  Margarita  |
