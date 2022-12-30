package com.example.project2.models.analysis

import com.google.gson.annotations.SerializedName

data class Disease(
    @SerializedName("Acute cholecystitis")
    val Acute_cholecystitis: String,
    @SerializedName("Acute pancreatitis triggered by alcohol")
    val Acute_pancreatitis_triggered_by_alcohol: String,
    @SerializedName("Bleeding peptic or duodenal ulcer")
    val Bleeding_peptic_or_duodenal_ulcer: String,
    @SerializedName("Chronic pancreatitis")
    val Chronic_pancreatitis: String,
    @SerializedName("Crohn's disease")
    val Crohns_disease : String,
    @SerializedName("Diabetes melltus type 1")
    val Diabetes_melltus_type_1: String,
    @SerializedName("Diabetes melltus type 2")
    val Diabetes_melltus_type_2: String,
    @SerializedName("Irritable bowel syndrome")
    val Irritable_bowel_syndrome: String,
    @SerializedName("Ulcerative colitis")
    val Ulcerative_colitis: String,
    @SerializedName("Ulcerative colitis exacerbation")
    val Ulcerative_colitis_exacerbation: String
)