package com.example.algoprojectlast;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javafx.stage.Stage;


import java.awt.Desktop;


import java.io.File;
import java.io.IOException;


import java.util.Optional;

//Image compressing = new Image("compress.png");
//                    ImageView loadingImageView = new ImageView(compressing);
//                    loadingImageView.setFitHeight(500);
//                    loadingImageView.setFitWidth(500);
//                    VBox loadingVBox = new VBox();
//                    loadingVBox.setAlignment(Pos.CENTER);
//                    loadingVBox.setSpacing(10);
//                    loadingVBox.getChildren().addAll(loadingImageView);
//                    compressInfoPane.setCenter(loadingVBox);
//                    BorderPane.setAlignment(loadingVBox, Pos.CENTER);
//                    BorderPane.setMargin(loadingVBox, new Insets(10, 10, 10, 10));
//
//                    backToMainPageButton.setDisable(true);
//                    openFileLocationButton.setDisable(true);
//                    stage.setScene(compressInfo);
//                    stage.setMaximized(true);
//                    stage.show();


public class Main extends Application {

    HuffmanEncoder huffmanEncoder;
    HuffmanDecoder huffmanDecoder;


    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage stage){


        BorderPane mainPagePane = new BorderPane();
        Scene mainPage = new Scene(mainPagePane, 1000, 450);
        mainPagePane.setPadding(new Insets(10, 10, 10, 10));
        mainPage.getStylesheets().add("style.css");


        BorderPane compressInfoPane = new BorderPane();
        Scene compressInfo = new Scene(compressInfoPane, 1200,600 );
        compressInfoPane.setPadding(new Insets(10, 10, 10, 10));
        compressInfo.getStylesheets().add("style.css");

        BorderPane decompressInfoPane = new BorderPane();
        Scene decompressInfo = new Scene(decompressInfoPane, 1200,600 );
        decompressInfoPane.setPadding(new Insets(10, 10, 10, 10));
        decompressInfo.getStylesheets().add("style.css");




        // ================================= compress Info =================================
        Label compressInfoLabel = new Label("Compressing Info");
        compressInfoLabel.setPadding(new Insets(50));
        compressInfoLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #ffffff");


        Label AsciiOccurrenceLabel = new Label("Ascii encoding");
        AsciiOccurrenceLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff");

        TableView<Record> AsciiOccurrenceTable = new TableView<>();
        AsciiOccurrenceTable.setEditable(false);
        AsciiOccurrenceTable.setPrefWidth(300);
        AsciiOccurrenceTable.setPrefHeight(300);

        TableColumn<Record, Character> AsciiOccurrenceTableAscii = new TableColumn<>("Ascii");
        AsciiOccurrenceTableAscii.setPrefWidth(100);
        AsciiOccurrenceTableAscii.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCh()));


        TableColumn<Record, Long> AsciiOccurrenceTableOccurrence = new TableColumn<>("Occurrence");
        AsciiOccurrenceTableOccurrence.setPrefWidth(100);
        AsciiOccurrenceTableOccurrence.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFreq()));


        TableColumn<Record, String> AsciiOccurrenceTableEncoding = new TableColumn<>("Code");
        AsciiOccurrenceTableEncoding.setPrefWidth(100);
        AsciiOccurrenceTableEncoding.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCode()));

        AsciiOccurrenceTable.getColumns().addAll(AsciiOccurrenceTableAscii, AsciiOccurrenceTableOccurrence, AsciiOccurrenceTableEncoding);


        VBox compressInfoVBox = new VBox();
        compressInfoVBox.setAlignment(Pos.CENTER);
        compressInfoVBox.setSpacing(10);
        compressInfoVBox.getChildren().addAll(AsciiOccurrenceLabel, AsciiOccurrenceTable);





        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("File Size");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Size (Bytes)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Compression Ratio");

        XYChart.Series<String, Number> originalFileSize = new XYChart.Series<>();
        originalFileSize.setName("Original File Size");


        XYChart.Series<String, Number> compressedFileSize = new XYChart.Series<>();
        compressedFileSize.setName("Compressed File Size");


        barChart.getData().addAll(originalFileSize, compressedFileSize);


        barChart.setPrefWidth(400);
        barChart.setPrefHeight(400);

        VBox sizeComparisonVBox = new VBox();
        sizeComparisonVBox.setAlignment(Pos.CENTER);
        sizeComparisonVBox.setSpacing(10);
        sizeComparisonVBox.getChildren().addAll( barChart);



        Label timeEstablished = new Label("Time est");
        timeEstablished.setMaxWidth(100);
        timeEstablished.setPrefWidth(100);
        timeEstablished.setMinWidth(100);
        timeEstablished.setStyle("""
                -fx-font-size: 16;
                    -fx-font-weight: Bold 700;
                    -fx-font-family: "Montserrat";
                    -fx-text-fill: #ffffff;""");

        Label extensionLabel = new Label("Extension");
        extensionLabel.setMaxWidth(100);
        extensionLabel.setPrefWidth(100);
        extensionLabel.setMinWidth(100);
        extensionLabel.setStyle("""
                -fx-font-size: 16;
                    -fx-font-weight: Bold 700;
                    -fx-font-family: "Montserrat";
                    -fx-text-fill: #ffffff;""");
        Label headerSizeLabel = new Label("Header Size");
        headerSizeLabel.setMaxWidth(100);
        headerSizeLabel.setPrefWidth(100);
        headerSizeLabel.setMinWidth(100);
        headerSizeLabel.setStyle("""
                -fx-font-size: 16;
                    -fx-font-weight: Bold 700;
                    -fx-font-family: "Montserrat";
                    -fx-text-fill: #ffffff;""");
        Label compressionRatio = new Label("Compression Ratio");
        compressionRatio.setMaxWidth(100);
        compressionRatio.setPrefWidth(100);
        compressionRatio.setMinWidth(100);
        compressionRatio.setStyle("""
                -fx-font-size: 16;
                    -fx-font-weight: Bold 700;
                    -fx-font-family: "Montserrat";
                    -fx-text-fill: #ffffff;""");
        Label sizeBeforeLabel = new Label("Size Before");
        sizeBeforeLabel.setMaxWidth(100);
        sizeBeforeLabel.setPrefWidth(100);
        sizeBeforeLabel.setMinWidth(100);
        sizeBeforeLabel.setStyle("""
                -fx-font-size: 16;
                    -fx-font-weight: Bold 700;
                    -fx-font-family: "Montserrat";
                    -fx-text-fill: #ffffff;""");
        Label sizeAfterLabel = new Label("Size After");
        sizeAfterLabel.setMaxWidth(100);
        sizeAfterLabel.setPrefWidth(100);
        sizeAfterLabel.setMinWidth(100);
        sizeAfterLabel.setStyle("""
                -fx-font-size: 16;
                    -fx-font-weight: Bold 700;
                    -fx-font-family: "Montserrat";
                    -fx-text-fill: #ffffff;""");

        Label headerText = new Label("Header");
        headerText.setMaxWidth(100);
        headerText.setPrefWidth(100);
        headerText.setMinWidth(100);
        headerText.setStyle("""
                -fx-font-size: 16;
                    -fx-font-weight: Bold 700;
                    -fx-font-family: "Montserrat";
                    -fx-text-fill: #ffffff;""");

        TextArea headerTextArea = new TextArea();
        headerTextArea.setPrefWidth(200);
        headerTextArea.setPrefHeight(100);
        headerTextArea.setEditable(false);

        VBox headerVBox = new VBox();
        headerVBox.setAlignment(Pos.CENTER);
        headerVBox.setSpacing(10);
        headerVBox.getChildren().addAll(headerText, headerTextArea);






        TextField timeEstablishedTextField = new TextField();
        timeEstablishedTextField.setEditable(false);
        TextField extensionTextField = new TextField();
        extensionTextField.setEditable(false);
        TextField headerSizeTextField = new TextField();
        headerSizeTextField.setEditable(false);
        TextField compressionRatioTextField = new TextField();
        compressionRatioTextField.setEditable(false);
        TextField sizeBeforeTextField = new TextField();
        sizeBeforeTextField.setEditable(false);
        TextField sizeAfterTextField = new TextField();
        sizeAfterTextField.setEditable(false);

        HBox timeEstablishedHBox = new HBox();
        timeEstablishedHBox.setSpacing(10);
        timeEstablishedHBox.getChildren().addAll(timeEstablished, timeEstablishedTextField);

        HBox extensionHBox = new HBox();
        extensionHBox.setSpacing(10);
        extensionHBox.getChildren().addAll(extensionLabel, extensionTextField);

        HBox headerSizeHBox = new HBox();
        headerSizeHBox.setSpacing(10);
        headerSizeHBox.getChildren().addAll(headerSizeLabel, headerSizeTextField);

        HBox HeaderHBox = new HBox();
        HeaderHBox.setSpacing(10);
        HeaderHBox.getChildren().addAll(compressionRatio, compressionRatioTextField);

        HBox sizeBeforeHBox = new HBox();
        sizeBeforeHBox.setSpacing(10);
        sizeBeforeHBox.getChildren().addAll(sizeBeforeLabel, sizeBeforeTextField);

        HBox sizeAfterHBox = new HBox();
        sizeAfterHBox.setSpacing(10);
        sizeAfterHBox.getChildren().addAll(sizeAfterLabel, sizeAfterTextField);

        VBox infoVbox = new VBox();
        infoVbox.setAlignment(Pos.CENTER);
        infoVbox.setSpacing(10);
        infoVbox.getChildren().addAll(timeEstablishedHBox, extensionHBox, headerSizeHBox, HeaderHBox, sizeBeforeHBox, sizeAfterHBox, headerVBox);

        HBox compressInfoHBox = new HBox();
        compressInfoHBox.setSpacing(50);
        compressInfoHBox.getChildren().addAll(infoVbox,compressInfoVBox, sizeComparisonVBox);
        compressInfoHBox.setAlignment(Pos.CENTER);

        Button backToMainPageButton = new Button("Back to Main Page");

        //buttons from other scene
        Button compressButton = new Button("Compress");
        Button decompressButton = new Button("Decompress");
        Button exitButton = new Button("Exit");


        backToMainPageButton.setOnAction(e -> {
            //clear all the text fields
            timeEstablishedTextField.clear();
            extensionTextField.clear();
            headerSizeTextField.clear();
            compressionRatioTextField.clear();
            sizeBeforeTextField.clear();
            sizeAfterTextField.clear();
            originalFileSize.getData().clear();
            compressedFileSize.getData().clear();
            headerTextArea.clear();
            AsciiOccurrenceTable.getItems().clear();

            compressButton.setDisable(false);
            decompressButton.setDisable(false);

            stage.setMaximized(false);
            stage.setScene(mainPage);
        });

        Button openFileLocationButton = new Button("Open File Location");
        openFileLocationButton.setOnAction(e -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                //view the file location with marked the file

                desktop.open(new File(huffmanEncoder.outputFilename.substring(0, huffmanEncoder.outputFilename.lastIndexOf("\\"))));

            } catch (IOException ioException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("File location not found");
                alert.showAndWait();
            }

        });

        HBox bottomButtons = new HBox();
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.getChildren().addAll(backToMainPageButton,new Pane(), openFileLocationButton);
        bottomButtons.setPadding(new Insets(30));
        bottomButtons.setSpacing(30);

        compressInfoPane.setTop(compressInfoLabel);
        BorderPane.setAlignment(compressInfoLabel, Pos.CENTER);
        compressInfoPane.setCenter(compressInfoHBox);
        BorderPane.setAlignment(compressInfoHBox, Pos.CENTER);
        compressInfoPane.setBottom(bottomButtons);
        BorderPane.setAlignment(bottomButtons, Pos.CENTER);

        compressInfoPane.setPadding(new Insets(10, 10, 10, 10));





        //==================================================================================================


        VBox leftButtons = new VBox();
        leftButtons.setPadding(new Insets(10, 10, 10, 10));
        leftButtons.setSpacing(10);
        leftButtons.setAlignment(Pos.CENTER);



        leftButtons.getChildren().addAll(compressButton, decompressButton, exitButton);



        HBox mainPageElements = new HBox();
        mainPageElements.setPadding(new Insets(10, 10, 10, 10));
        mainPageElements.setSpacing(30);
        mainPageElements.getChildren().addAll(leftButtons);
        mainPageElements.setAlignment(Pos.CENTER);


        mainPagePane.setCenter(mainPageElements);
        BorderPane.setAlignment(mainPageElements, Pos.CENTER);
        BorderPane.setMargin(mainPageElements, new Insets(10, 10, 10, 10));




        compressButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a file to compress");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*"),
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            File file = fileChooser.showOpenDialog(stage);

            try {
                if(file != null){

                    compressButton.setDisable(true);
                    decompressButton.setDisable(true);


                    try {

                        huffmanEncoder = new HuffmanEncoder(file);

                        //run the huffman encoder on separate thread ,to avoid freezing the UI

                        Task<Void> task = new Task<>() {
                            @Override
                            protected Void call() throws Exception {
                                try {
                                    huffmanEncoder.encodeFile();
                                } catch (IOException ioException) {
                                    throw new Exception("Error in encoding file");
                                }

                                return null;
                            }
                        };

                        task.setOnSucceeded(event -> {
                            // Get the time taken to encode the file


                            backToMainPageButton.setDisable(false);
                            openFileLocationButton.setDisable(false);
                            compressInfoPane.setCenter(compressInfoHBox);
                            stage.setScene(compressInfo);
                            stage.setMaximized(true);

                            timeEstablishedTextField.setText(huffmanEncoder.getTimeTaken() + " ms");
                            extensionTextField.setText(huffmanEncoder.getExtension());
                            headerSizeTextField.setText(huffmanEncoder.getHeaderSize()+ " bytes");

                            //ratio to 2 decimal places
                            compressionRatioTextField.setText(String.format("%.2f", huffmanEncoder.getCompressionRatio()) + " %");
                            sizeBeforeTextField.setText(huffmanEncoder.getInputFileSize() + " bytes");
                            sizeAfterTextField.setText(huffmanEncoder.getOutputFileSize() + " bytes");

                            originalFileSize.getData().add(new XYChart.Data<>("Original File Size", huffmanEncoder.getInputFileSize()));
                            compressedFileSize.getData().add(new XYChart.Data<>("Compressed File Size", huffmanEncoder.getOutputFileSize()));


                            headerTextArea.setText(huffmanEncoder.getHeader());

                            ObservableList<Record> data = FXCollections.observableArrayList();
                            for (int i = 0; i < huffmanEncoder.getCharFrequencies().length; i++) {
                                if (huffmanEncoder.getCharFrequencies()[i] > 0) {
                                    data.add(new Record((char) i, huffmanEncoder.getCharFrequencies()[i], huffmanEncoder.getHuffmanCodes()[i]));
                                }
                            }
                            AsciiOccurrenceTable.setItems(data);



                        });

                        // Start the task on a separate thread
                        Thread thread = new Thread(task);
                        thread.start();




                    } catch (Exception ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("An error occurred while compressing the file");
                        alert.setContentText(ex.getMessage());
                        alert.showAndWait();
                    }

                }

            }catch (Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error occurred while compressing the file");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }

        });

        decompressButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a file to decompress");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Huffman Files", "*.huffy"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            File file = fileChooser.showOpenDialog(stage);
            if(file != null){

                compressButton.setDisable(true);
                decompressButton.setDisable(true);


                huffmanDecoder = new HuffmanDecoder(file);


                try {

                    //run the huffman decoder on separate thread ,to avoid freezing the UI
                    Task<Void> task = new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            try {
                                huffmanDecoder.decodeFile();
                            } catch (IOException ioException) {
                                throw new Exception("Error in decoding file");
                            }

                            return null;
                        }
                    };

                    task.setOnSucceeded(event -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Decompression Successful");
                        alert.setHeaderText("The file has been decompressed successfully");
                        alert.setContentText("do you want to open the file location?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK){
                            try {
                                Desktop.getDesktop().open(new File(huffmanDecoder.getOutputFileName().substring(0, huffmanDecoder.getOutputFileName().lastIndexOf("\\"))));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                        compressButton.setDisable(false);
                        decompressButton.setDisable(false);

                        System.out.println(huffmanDecoder.getTimeTaken());

                    });

                    // Start the task on a separate thread
                    Thread thread = new Thread(task);
                    thread.start();



                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("An error occurred while decompressing the file");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            }
        });


        exitButton.setOnAction(e -> {
            System.exit(0);
        });



        //==================================================================================================



        stage.setScene(mainPage);
        stage.setTitle("Algo Project 2");
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }



}