package com.example.project2.models.analysis

import com.google.gson.annotations.SerializedName

data class VariableImportance(
    @SerializedName("Acute cholecystitis")
    val Acute_cholecystitis: List<List<String>>,
    @SerializedName("Acute pancreatitis triggered by alcohol")
    val Acute_pancreatitis_triggered_by_alcohol: List<List<String>>,
    @SerializedName("Bleeding peptic or duodenal ulcer")
    val Bleeding_peptic_or_duodenal_ulcer: List<List<String>>,
    @SerializedName("Chronic pancreatitis")
    val Chronic_pancreatitis: List<List<String>>,
    @SerializedName("Crohn's disease")
    val Crohn_disease: List<List<String>>,
    @SerializedName("Diabetes melltus type 1")
    val Diabetes_melltus_type_1: List<List<String>>,
    @SerializedName("Diabetes melltus type 2")
    val Diabetes_melltus_type_2: List<List<String>>,
    @SerializedName("Irritable bowel syndrome")
    val Irritable_bowel_syndrome: List<List<String>>,
    @SerializedName("Ulcerative colitis")
    val Ulcerative_colitis: List<List<String>>,
    @SerializedName("Ulcerative colitis exacerbation")
    val Ulcerative_colitis_exacerbation: List<List<String>>
)