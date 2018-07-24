"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var protractor_1 = require("protractor");
var BlackBearPage = (function () {
    function BlackBearPage() {
    }
    BlackBearPage.prototype.navigateTo = function () {
        return protractor_1.browser.get('/');
    };
    BlackBearPage.prototype.getParagraphText = function () {
        return protractor_1.element(protractor_1.by.css('app-root h1')).getText();
    };
    return BlackBearPage;
}());
exports.BlackBearPage = BlackBearPage;
//# sourceMappingURL=app.po.js.map