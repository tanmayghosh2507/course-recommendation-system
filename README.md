# course-recommendation-system
A recommendation system built in Java using Naive Bayes Model.

## Domain:
Limited to Grraduate courses of Computer Science Department of Iowa State University. But can be used for any catalog after slight modifications in the Crawling.java

## Team Members:
1. Tanmay Kumar Ghosh, Graduate Student, Iowa State University
2. Nikhil Bansal, Graduate Student, Iowa State University
3. Nitesh Gupta, Graduate Student, Iowa State University

## Instructions on how to run the application:
1. The source files are located inside src. Import the project and go to src.
2. Run crawler.java to generate the course.tsv course list.
3. Run Model.java to generate all model files. It will be added in the source folder.
4. Run CourseRecommender.java file with the following arguments: title_model title_term_prob desc_model desc_term_prob
5. Provide your research interest as input after choosing option 2(From console).

Currently, the model files are present in the repository, just for reference. But it is recommended to generate these again using the steps mentioned above due to timely changes in catalog etc.
