'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

useCase('Browse page variants')
    .description('On the step page the user can flip through all existing variants of the page.')
    .describe(function () {

        var stepPage = new pages.stepPage();
        var objectDetailsPage = new pages.objectDetailsPage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('Browse variants')
            .description('Navigate to previous / next page variants.')
            .it(function () {
                stepPage.goToPage('/step/Switch%20Language/search_article_in_german_and_switch_to_spanish/contentPage.jsp/0/0');
                stepPage.assertPageVariantIndicatorValue('Page-Variant 8 of 10');
                step('A step of the contentPage.jsp page.');

                stepPage.goToPreviousPageVariant();
                stepPage.assertPageVariantIndicatorValue('Page-Variant 7 of 10');
                step('A different page variant.');

                stepPage.goToNextPageVariant();
                stepPage.goToNextPageVariant();
                stepPage.goToNextPageVariant();
                stepPage.assertPageVariantIndicatorValue('Page-Variant 10 of 10');
                stepPage.assertNextPageVariantButtonIsDisabled();
                step('The last page variant. Button for next page variant is disabled.');
            });

        scenario('Variant Overview')
            .description('Go to page variants overview page to select a different page variant.')
            .it(function () {
                stepPage.goToPage('/step/Switch%20Language/search_article_in_german_and_switch_to_spanish/contentPage.jsp/0/0');
                step('A step of the contentPage.jsp page.');

                stepPage.clickAllPageVariantsLink();
                stepPage.assertRoute('/object/page/contentPage.jsp');
                step('Overview of page variants in all use cases.');

                objectDetailsPage.clickToExpand(3);
                step('Expanded scenario with the steps that contain the page.');

                objectDetailsPage.clickNthTreeTableRow(6);
                stepPage.assertRoute('/step/Find%20Page/find_page_title_ambiguous_directly/contentPage.jsp/0/2');
                step('A different page variant.');
            });

    });
