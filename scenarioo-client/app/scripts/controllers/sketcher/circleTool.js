/*scenarioo-client
 Copyright (C) 2015, scenarioo.org Development Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';

angular.module('scenarioo.controllers').factory('CircleTool', function(DrawingPadService) {

    var name = 'Circle Tool';
    var icon = null;
    var tooltip = 'This tool is used to draw circles.';
    var cursor = null;
    var newCircle = null;
    var mousePosX1 = 0;
    var mousePosY1 = 0;
    var mousePosLastX = 0;
    var mousePosLastY = 0;
    var mousedown = false;
    var drawingPad = DrawingPadService.get;



    function onmousedown(event) {
        mousedown = true;
        newCircle = drawingPad.circle(0);


        mousePosX1 = event.offsetX;
        mousePosY1 = event.offsetY;

        newCircle.attr({
          cx: mousePosX1,
          cy: mousePosY1,
          fill: '#f60'
        });

        mousePosLastX = mousePosX1;
        mousePosLastY = mousePosY1;
      }

    function onmouseup(event) {
        mousedown = false;

        newCircle.attr('fill', '#0f3');
        mousePosX1 = 0;
        mousePosY1 = 0;
        mousePosLastX = 0;
        mousePosLastY = 0;

        //toolDeactivated();
      }

    function onmousedrag(event) {
        if (!mousedown) {
          return;
        }

        var delta = 0;
        var offsetToOriginX = event.offsetX - mousePosX1;
        var offsetToOriginY = event.offsetY - mousePosY1;
        var dx = 0;
        var dy = 0;

        if (offsetToOriginX > 0 && offsetToOriginY > 0){
          dx = event.offsetX - mousePosLastX;
          dy = event.offsetY - mousePosLastY;
        } else if (offsetToOriginX > 0 && offsetToOriginY < 0){
          dx = event.offsetX - mousePosLastX;
          dy = mousePosLastY - event.offsetY;
        } else if (offsetToOriginX < 0 && offsetToOriginY < 0){
          dx = mousePosLastX - event.offsetX;
          dy = mousePosLastY - event.offsetY;
        } else {
          dx = mousePosLastX - event.offsetX;
          dy = event.offsetY - mousePosLastY;
        }

        delta = ((dx + dy) / 2);
        if (delta > 10){
          delta = 10;
        } else if (delta < -20){
          delta = -20;
        }

        var rx = newCircle.attr('rx');
        if (delta < (rx * -1)){
          delta = rx * -1;
        }

        newCircle.attr({
          rx: newCircle.attr('rx') + delta,
          ry: newCircle.attr('ry') + delta
        });

        //console.log(newRect.attr('width'), newRect.attr('height'), dx, dy);

        mousePosLastX = event.offsetX;
        mousePosLastY = event.offsetY;
      }

    return {

      name: name,
      icon: icon,
      tooltip: tooltip,
      cursor: cursor,

      onmouseup: onmouseup,
      onmousedown: onmousedown,
      onmousedrag: onmousedrag


    };

});
