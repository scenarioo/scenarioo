# Order: 1
# Milestone: R1

Feature: Basic Pricing
  In order to have simple basic pizza prices
  the basic price claculation just returns default pizza price
  depending on country where the pizza is ordered.

  This prices will be fixed in a first version,
  later producers might customize their own prices.

  Scenario Outline: Basic Pizza Prices in Switzerland
    Given our default pizza prices
    And no customized prices per producers
    When I order Pizza <pizza>
    And I choose a producer in Switzerland
    Then the price will be <price> <currency>

    Examples:
      | pizza | price | currency |
      | Margarita           |  12.50  |  CHF  |
      | Funghi              |  14.00  |  CHF  |
      | Napoli              |  14.50  |  CHF  |
      | Prosciutto          |  15.00  |  CHF  |
      | Prosciutto & Funghi |  16.00  |  CHF  |
      | Quattro Stagioni    |  17.00  |  CHF  |
      | Diavolo             |  16.00  |  CHF  |

  Scenario Outline: Basic Pizza Prices in Germany
    Given our default pizza prices
    And no customized prices per producers
    When I order Pizza <pizza>
    And I choose a producer in Germany
    Then the price will be <price> <currency>

    Examples:
      | pizza | price | currency |
      | Margarita           |  8.00  |  EUR  |
      | Funghi              |  9.00  |  EUR  |
      | Napoli              |  9.00  |  EUR  |
      | Prosciutto          |  9.50  |  EUR  |
      | Prosciutto & Funghi |  9.90  |  EUR  |
      | Quattro Stagioni    |  10.50 |  EUR  |
      | Diavolo             |  9.90  |  EUR  |
