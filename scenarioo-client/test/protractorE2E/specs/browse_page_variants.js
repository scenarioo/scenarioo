'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

describeUseCaseE('Browse page variants', {
    description: 'On the step page the user can flip through all existing variants of the page.'
}, function () {

    var stepPage = new pages.stepPage();
    var objectDetailsPage = new pages.objectDetailsPage();

    beforeEach(function(){
        new pages.homePage().initLocalStorage();
    });

    describeScenarioE('Navigate to previous / next page variants.', function () {
        stepPage.goToPage('/step/Switch%20Language/search_article_in_german_and_switch_to_spanish/contentPage.jsp/0/0');
        stepPage.assertPageVariantIndicatorValue('Page-Variant 8 of 10');
        scenarioo.saveStep('A step of the contentPage.jsp page.');

        stepPage.goToPreviousPageVariant();
        stepPage.assertPageVariantIndicatorValue('Page-Variant 7 of 10');
        scenarioo.saveStep('A different page variant.');

        stepPage.goToNextPageVariant();
        stepPage.goToNextPageVariant();
        stepPage.goToNextPageVariant();
        stepPage.assertPageVariantIndicatorValue('Page-Variant 10 of 10');
        stepPage.assertNextPageVariantButtonIsDisabled();
        scenarioo.saveStep('The last page variant. Button for next page variant is disabled.');
    });

    describeScenarioE('Go to page variants overview page to select a different page variant.', function () {
        stepPage.goToPage('/step/Switch%20Language/search_article_in_german_and_switch_to_spanish/contentPage.jsp/0/0');
        scenarioo.saveStep('A step of the contentPage.jsp page.');

        stepPage.clickAllPageVariantsLink();
        stepPage.assertRoute('/object/page/contentPage.jsp');
        scenarioo.saveStep('Overview of page variants in all use cases.');

        objectDetailsPage.clickToExpand(3);
        scenarioo.saveStep('Expanded scenario with the steps that contain the page.');

        objectDetailsPage.clickNthTreeTableRow(6);
        stepPage.assertRoute('/step/Find%20Page/find_page_title_ambiguous_directly/contentPage.jsp/0/2');
        scenarioo.saveStep('A different page variant.');
    });

});
