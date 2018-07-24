import { BlackBearPage } from './app.po';

describe('black-bear App', () => {
  let page: BlackBearPage;

  beforeEach(() => {
    page = new BlackBearPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
