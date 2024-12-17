import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Survey class
class Survey {
    private int id;
    private String name;
    private String description;
    private List<Question> questions;

    public Survey(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.questions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }
}

// Question class
class Question {
    private int id;
    private String text;
    private List<ResponseOption> responseOptions;

    public Question(int id, String text) {
       this.id = id;
        this.text = text;
        this.responseOptions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<ResponseOption> getResponseOptions() {
        return responseOptions;
    }

    public void addResponseOption(ResponseOption responseOption) {
        responseOptions.add(responseOption);
    }
}

// ResponseOption class
class ResponseOption {
    private int id;
    private String text;
    private int value;

    public ResponseOption(int id, String text, int value) {
        this.id = id;
        this.text = text;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}

// UserResponse class
class UserResponse {
    private int id;
    private int surveyId;
    private int questionId;
    private int responseOptionId;
    private int userId;

    public UserResponse(int id, int surveyId, int questionId, int responseOptionId, int userId) {
        this.id = id;
        this.surveyId = surveyId;
        this.questionId = questionId;
        this.responseOptionId = responseOptionId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public int getResponseOptionId() {
        return responseOptionId;
    }

    public int getUserId() {
        return userId;
    }
}

// SurveyResult class
class SurveyResult {
    private int id;
    private int surveyId;
    private double averageRating;

    public SurveyResult(int id, int surveyId, double averageRating) {
        this.id = id;
        this.surveyId = surveyId;
        this.averageRating = averageRating;
    }

    public int getId() {
        return id;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public double getAverageRating() {
        return averageRating;
    }
}

// SurveyService class
class SurveyService {
    private Map<Integer, Survey> surveys;
    private Map<Integer, UserResponse> userResponses;
    private Map<Integer, SurveyResult> surveyResults;

    public SurveyService() {
        this.surveys = new HashMap<>();
        this.userResponses = new HashMap<>();
        this.surveyResults = new HashMap<>();
    }

    public void createSurvey(Survey survey) {
        surveys.put(survey.getId(), survey);
    }

    public void addUserResponse(UserResponse userResponse) {
        userResponses.put(userResponse.getId(), userResponse);
    }

    public SurveyResult calculateSurveyResult(int surveyId) {
        double sum = 0;
        int count = 0;

        for (UserResponse userResponse : userResponses.values()) {
            if (userResponse.getSurveyId() == surveyId) {
                ResponseOption responseOption = getResponseOption(userResponse.getResponseOptionId());
                sum += responseOption.getValue();
                count++;
            }
        }

        double averageRating = sum / count;
        SurveyResult surveyResult = new SurveyResult(surveyId, surveyId, averageRating);
        surveyResults.put(surveyId, surveyResult);
        return surveyResult;
    }

    private ResponseOption getResponseOption(int responseOptionId) {
        // Assume we have a method to get the response option from the database
        // For simplicity, let's just return a dummy response option
        return new ResponseOption(responseOptionId, "Dummy Response Option", 5);
    }
}

public class RatingService {
    public static void main(String[] args) {
        SurveyService surveyService = new SurveyService();

        // Create a survey
        Survey survey = new Survey(1, "Sample Survey", "This is a sample survey");
        surveyService.createSurvey(survey);

        // Add questions to the survey
        Question question1 = new Question(1, "How was your experience?");
        question1.addResponseOption(new ResponseOption(1, "Excellent", 5));
        question1.addResponseOption(new ResponseOption(2, "Good", 4));
        question1.addResponseOption(new ResponseOption(3, "Fair", 3));
        question1.addResponseOption(new ResponseOption(4, "Poor", 2));
        question1.addResponseOption(new ResponseOption(5, "Very Poor", 1));
        survey.addQuestion(question1);

        Question question2 = new Question(2, "Would you recommend our service?");
        question2.addResponseOption(new ResponseOption(6, "Yes", 5));
        question2.addResponseOption(new ResponseOption(7, "No", 1));
        survey.addQuestion(question2);

        // Add user responses
        UserResponse userResponse1 = new UserResponse(1, 1, 1, 1, 1);
        surveyService.addUserResponse(userResponse1);

        UserResponse userResponse2 = new UserResponse(2, 1, 1, 2, 1);
        surveyService.addUserResponse(userResponse2);

        UserResponse userResponse3 = new UserResponse(3, 1, 2, 6, 1);
        surveyService.addUserResponse(userResponse3);

        // Calculate survey result
        SurveyResult surveyResult = surveyService.calculateSurveyResult(1);
        System.out.println("Survey Result:");
        System.out.println("Average Rating: " + surveyResult.getAverageRating());
    }
}