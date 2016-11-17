
module.exports = function(grunt){
	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),

    watch: {
       options: { livereload: true },
       css: {
        files: ["css/main.css"],
        tasks: ['cssmin']
       },
      js: {
        files: ['js/app.js','js/controllers/MainController.js'],
        tasks: ['concat','uglify'],
      }, 
    },

    concat: {
    js: {
        src: ['js/app.js', 'js/controllers/MainController.js'],
        dest: 'js/main-concat.js'
    }
},


      uglify: {
        dist: {
          src: ["js/main-concat.js"],
          dest: 'js/main-min.js'
        }

      },

      cssmin: {
        dist: {
          src: ["css/main.css"],
          dest: 'css/main-min.css'
        }
      },




	});
	grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-open');
 // grunt.loadNpmTasks('grunt-contrib-livereload');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
 // grunt.loadNpmTasks('grunt-open');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.registerTask("default", ["concat",'server', 'open', 'watch']);
};