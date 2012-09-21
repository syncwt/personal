App = Em.Application.create();

App.Person = Ember.Object.extend({
    firstName: null,
    lastName: null,
    fullName: function() {
    return this.get('firstName') +
               " " + this.get('lastName');
      }.property('firstName', 'lastName')
    });

App.Me = App.Person.create({
    firstName: 'Cedric',
    lastName: 'Beust'
});
