# Order: 1
# Milestone: R1

Feature: Create Collection Order

  Scenario Outline: Create Collection Order
    Given I am loggedin as a registered user
    When I create a collection order
    And I choose the Pizza producer for that order
    Then I will receive an email with text
        """
        Dear customer,

        You started a collection order on PizzaaaS.

        Send the following link to your friends to add their pizzas to that order:
        http://www.pizzaaas.com/collectionOrder/fakeCollectionOrderIdOnTestSystem

        You can choose when you want to finally submit the order.

        Best regards,
        Your PizzaaaS Team
        """
    And when clicking on the survey link I can see a list of available pizzas for the choosen producer

