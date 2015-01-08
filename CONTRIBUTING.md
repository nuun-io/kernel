# Contributing to Nuun IO

We'd love for you to contribute to our project making Nuun even better than it is today. Here are few guidelines which
would help you.

## About GitHub

Make sure to know the basics about GitHub. Take a look at this [guide](https://guides.github.com/activities/contributing-to-open-source/).

Make sure to know how to use pull requests. See this [help documentation](https://help.github.com/articles/using-pull-requests/).

## Got a Question or Problem?

If you have questions about how to use Nuun, please direct these to [StackOverflow](http://stackoverflow.com/).

## Found an Issue?

If you find a bug in the source code or a mistake in the documentation, you can help us by submitting an issue to our
GitHub Repository. Even better you can submit a Pull Request with a fix.

## Want a feature?

You can request a new feature by submitting an issue to our GitHub Repository. If you would like to implement a new
feature then consider what kind of change it is, discuss it with us before hand in your issue, so that we can better
coordinate our efforts, prevent duplication of work, and help you to craft the change so that it is successfully
accepted into the project.

## Submission guidelines

### Submitting an issue

Before you submit your issue search the archive, maybe your question was already answered.

If your issue appears to be a bug, and hasn't been reported, open a new issue. Help us to maximize the effort by not
reporting duplicate issues. Providing the following information will increase the chances of your issue being dealt
with quickly:

- Motivation for or Use Case - explain why this is a bug for you
- Nuun Version(s) - is it a regression?
- JDK - is this a problem with all JDK?
- Reproduce the Error - provide a test case or a unambiguous set of steps.

### Submitting a pull request

Before you submit your pull request consider the following guidelines:

- Search GitHub for an open or closed Pull Request that relates to your submission. You don't want to duplicate effort.
- Make your changes in a new git branch
- Add JUnit test cases for all behavior changes
- Commit your changes using a descriptive commit message
- Build and test your changes locally (`mvn clean install`)
- Push your branch to GitHub
- In GitHub send a pull request
- If we suggest changes then
 - Make the required updates.
 - Re-run the tests
 - Rebase your branch and force push to your GitHub repository (this will update your Pull Request)
