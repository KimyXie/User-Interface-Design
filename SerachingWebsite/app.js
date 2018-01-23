/*
 *  Starter code for University of Waterloo CS349 - Spring 2017 - A3.
 *	Refer to the JS examples shown in lecture for further reference.
 *  Note: this code uses ECMAScript 6.
 *  Updated 2017-07-12.
 */
	
"use strict";

// Get your own API key from https://uwaterloo.ca/api/register
var apiKey = 'cea74e28c29f399382c949ccd1128e2b';
const endPoint = 'https://api.uwaterloo.ca/v2/terms';

(function(exports) {

	/* A Model class */
    class AppModel {
		constructor() {
			this._observers = [];
            this.examList = [];
            this.termList = [];
            this.screenView = "examPage";
            this.oldScreenView = "";
            this.term = 1175;
            this.filter = "";
		}

        // You can add attributes / functions here to store the data
        setfilter(filter){
            this.filter = filter;
            this.notify();
        }

        // Call this function to retrieve data from a UW API endpoint
        loadExamData() {
            var that = this;
            $.getJSON('https://api.uwaterloo.ca/v2/terms/' + this.term +  '/examschedule.json' + "?key=" + apiKey,
                function (data) {
                    // Do something with the data; probably store it
                    // in the Model to be later read by the View.
                    // Use that (instead of this) to refer to the instance
                    // of AppModel inside this function.
                    that.examList = data.data;

                    that.notify(); // Notify View(s)
                }
            );
        }
        loadTermData() {
            var that = this;
            $.getJSON('https://api.uwaterloo.ca/v2/terms/' + this.term +  '/courses.json' + "?key=" + apiKey,
                function (data) {
                    that.termList = data.data;
                    that.notify(); // Notify View(s)
                }
            );
        }

        sorting(choice){
            if (choice == "CourseName +"){
                this.examList.sort(function (a, b){
                    if (a.course > b.course){
                        return 1;
                    } 
                    else{
                        return -1;
                    }
                })
            }
            else if (choice == "CourseName -"){
                this.examList.sort(function (a, b){
                    if (a.course < b.course){
                        return 1;
                    } 
                    else{
                        return -1;
                    }
                })
            }
            else if (choice == "Date +"){
                this.examList.sort(function (a, b){
                    if (a.sections[0].date > b.sections[0].date){
                        return 1;
                    } 
                    else{
                        return -1;
                    }
                })
            }
            else if (choice == "Date -"){
                this.examList.sort(function (a, b){
                    if (a.sections[0].date < b.sections[0].date){
                        return 1;
                    } 
                    else{
                        return -1;
                    }
                })
            }
            else if (choice == "Location +"){
                this.examList.sort(function (a, b){
                    if (a.sections[0].location > b.sections[0].location){
                        return 1;
                    } 
                    else{
                        return -1;
                    }
                })
            }
            else if (choice == "Location -"){
                this.examList.sort(function (a, b){
                    if (a.sections[0].location < b.sections[0].location){
                        return 1;
                    } 
                    else{
                        return -1;
                    }
                })
            }

            this.notify();
        }

		// Add observer functionality to AppModel objects:
		
		// Add an observer to the list
		addObserver(observer) {
            this._observers.push(observer);
            observer.updateView(this, null);
        }
		
		// Notify all the observers on the list
		notify(args) {
            _.forEach(this._observers, function(obs) {
                obs.updateView(this, args);
            });
        }
    }

    /*
     * A view class.
     * model:  the model we're observing
     * div:  the HTML div where the content goes
     */
    class AppView {
		constructor(model, div) {
			this.model = model;
			this.div = div;
            this.newDiv = $('<div></div>');
            this.hasSearchBar = false;
			model.addObserver(this); // Add this View as an Observer
            

            var that = this;

            $('#SelectingTerm').change(function(){
                that.model.term = $('#SelectingTerm').val();
                if(that.model.screenView == "examPage") {
                    that.model.loadExamData();
                }
                else if(that.model.screenView == "termPage"){
                    that.model.loadTermData();
                }

            });
            // $('#SelectingFilter').change(function(){
            //     that.model.sorting($('#SelectingFilter').val());

            // });
            $('#examPage').click(function(){
                that.model.oldScreenView = that.model.screenView;
                that.model.screenView = "examPage";
                that.hasSearchBar = false;
                that.model.loadExamData();
            });
            $('#termPage').click(function(){
                that.model.oldScreenView = that.model.screenView;
                that.hasSearchBar = false;
                that.model.screenView = "termPage";
                that.model.loadTermData();
            });

            $(document).on({
                ajaxStart: function(){
                    $("body").addClass("loading");
                },
                ajaxStop: function(){
                    $("body").removeClass("loading");
                }
            });

		}
		
        updateView(obs, args) {
            // Add code here to update the View
            if(this.model.screenView == "examPage") {
                var that = this;
                if(this.model.oldScreenView != "examPage" && this.hasSearchBar == false){
                    $(this.div).empty();
                    console.log("adding exam search bar");
                    var filterDropdown = $('<div id="ChoiceOfFilter">' +
                                            '<select id="SelectingFilter">' +
                                            '<option selected="selected" value="CourseName +">Course Name +</option>'+
                                            '<option value="CourseName -">Course Name -</option>'+
                                            '<option value="Date +">Date +</option>'+
                                            '<option value="Date -">Date -</option>'+
                                            '<option value="Location +">Location +</option>'+
                                            '<option value="Location -">Location -</option>'+
                                            '</select>'+
                                        '</div>');
                    filterDropdown.find('select').change(function(){
                        that.model.sorting(filterDropdown.find('select').val());
                    });
                    $(this.div).append(filterDropdown);

                    var searchbar = $('<div id="SearchBar">' +
                                        '<input id="searchbar" type="search" class="keywords search-query" placeholder="eg: cs, 350, cs 350" value="">' +
                                        '<i class="icon-search"></i>' +
                                    '</div>');
                    searchbar.find("input").keyup(function(){
                        that.model.setfilter(searchbar.find("input").val());
                    });
                    this.hasSearchBar = true;
                    $(this.div).append(searchbar);
                    $(this.div).append(this.newDiv);
                }
                
                $(this.newDiv).empty();
                $(this.newDiv).append('<div class = "row-label">' +
                            '<div class= "courseView">' + "Course" + " " + '</div>' +
                            '<div class= "dateView">' + "Date" + '</div>' +
                            '<div class= "startTimeView">' + "Time" + '</div>' +
                            '<div class= "locationView">' + "Location" + '</div>'
                             + "</div>");
                for (var each of this.model.examList) {
                    //$(this.div).append('<ol id="examList" style="opacity: 1;"></ol>');
                    if (each.course.toLowerCase().indexOf(this.model.filter.toLowerCase()) != -1) {
                        $(this.newDiv).append('<div class = "row">' +
                            '<div class= "courseView">' + each.course + " " + '</div>' +
                            
                            '<div class= "dateView">' + each.sections[0].date + '</div>' +
                            '<div class= "startTimeView">' + each.sections[0].start_time + " - " + each.sections[0].end_time + '</div>' +
                          
                            '<div class= "locationView">' + each.sections[0].location + '</div>'
                             + "</div>");
                    }
                }
            }
            if(this.model.screenView == "termPage"){
                var that = this;
                if(this.model.oldScreenView != "termPage" && this.hasSearchBar == false){
                    $(this.div).empty();
                    console.log("adding term search bar");
                    var searchbar = $('<div id="SearchBar">' +
                                        '<input id="searchbar" type="search" class="keywords search-query" placeholder="eg: cs, 349, User Interface" value="">' +
                                        '<i class="icon-search"></i>' +
                                    '</div>');
                    searchbar.find("input").keyup(function(){
                        that.model.setfilter(searchbar.find("input").val());
                    });
                    this.hasSearchBar = true;
                    $(this.div).append(searchbar);
                    $(this.div).append(this.newDiv);
                }
                $(this.newDiv).empty();
                $(this.newDiv).append('<div class = "row-label">' +
                            '<div class= "subjectView">' + "Course" + " " + '</div>' +
                            '<div class= "titleView">' + "Title" + '</div>'
                             + "</div>");
                //termPage
                for (var each of this.model.termList) {
                    if (each.subject.toLowerCase().indexOf(this.model.filter.toLowerCase()) != -1 ||
                        each.catalog_number.toLowerCase().indexOf(this.model.filter.toLowerCase()) != -1 ||
                        each.title.toLowerCase().indexOf(this.model.filter.toLowerCase()) != -1) {
                        $(this.newDiv).append('<div class = "termRow">' +
                            '<div class = "subjectView">' + each.subject + " " + each.catalog_number + '</div>' +
                            '<div class = "titleView">' + each.title + '</div>' + '</div>' + "</div>");
                    }
                }
            }

        };
    }

	/*
		Function that will be called to start the app.
		Complete it with any additional initialization.
	*/
    exports.startApp = function() {
        var model = new AppModel();
        var view = new AppView(model, "#viewContent");
        model.loadExamData();
    }

})(window);
