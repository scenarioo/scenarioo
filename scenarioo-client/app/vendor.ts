require('../node_modules/bootstrap/dist/css/bootstrap.min.css');
require('./components/font-awesome/css/font-awesome.min.css');

import 'jquery';
import 'angular';
import 'angular-resource';
import 'angular-sanitize';
import 'angular-route';
import 'angular-ui-bootstrap';
import 'angular-local-storage';
import 'twigs';

require('./components/angular-unsavedChanges/dist/unsavedChanges.js');
require('./components/svg.select.js/dist/svg.select.css');
(<any>window).SVG = require('./components/svg.js/dist/svg');
require('./components/svg.select.js/dist/svg.select');
require('./components/svg.draggable.js/dist/svg.draggable');
require('./components/svg.resize.js/dist/svg.resize');
