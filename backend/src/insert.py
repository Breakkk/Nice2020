def insert(QuestionID, SubjectName, TypeName, FilePath, ShiTiShow, ShiTiAnalysis="", ShiTiAnswer=""):
    sql_insert = "insert into TK_QuestionInfo(\
        QuestionID\
        ,WebSiteID\
        ,SubjectID\
        ,SubjectName\
        ,TextBookID\
        ,TextBookName\
        ,DBCode\
        ,TypeID\
        ,TypeName\
        ,BaseTypeID\
        ,QuestionDifficult\
        ,QuestionDifferent\
        ,QuestionDegrade\
        ,QuestionCommand\
        ,QuestionStandard\
        ,QuestionTitle\
        ,QuestionKeyword\
        ,QuestionsSource\
        ,QuestionText\
        ,QuestionTextControl\
        ,QuestionSubNum\
        ,AnswerText\
        ,AnswerControl\
        ,AnswerAnalysisText\
        ,AnalysisControl\
        ,AnnexText\
        ,AnnexControl\
        ,Secrecy\
        ,AnswerTime\
        ,Score\
        ,PointCode\
        ,PointName\
        ,DictionaryCode\
        ,DictionaryName\
        ,UsedTime\
        ,ModifiedDifficulty\
        ,ModifiedDiscrimination\
        ,MarkingMachine\
        ,MarkingMachineValue\
        ,CompanyID\
        ,CheckLink\
        ,CheckNumber\
        ,Price\
        ,PriceUnit\
        ,PriceStatus\
        ,IsUse\
        ,DeleteFlag\
        ,UpdateCount\
        ,CreateUser\
        ,UpdateUser\
        ,QuestionIDCopy\
        ,Click\
        ,GradeCode\
        ,GradeName\
        ,GradeBookCode\
        ,GradeBookName\
        ,questionid1\
        ,questionidcopy1\
        ,AnnexTextCopy1\
        ,AnnexTextCopy2\
        ,ContentMark\
        ,FilePath\
        ,QuestionIDCopyHistory\
        ,OrderNum\
        ,ShiTiShow1\
        ,ShiTiAnswer\
        ,ShiTiAnalysis\
        ,ShiTiShow\
        ,CreateUserBack\
        ,UnitID\
        ,ShareTag\
        ,DistrictCode\
        ,ChannelName\
        ,ChannelCode\
        ,ProductID\
        ,IsPortalShow\
        ,checkUser\
        ,agentId\
        ,editAnswerFlag\
        ,solved) \
        values(\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}',\
            '{}', \
            '{}',\
            '{}', \
            '{}', \
            '{}',\
            '{}',\
            '{}',\
            '{}', \
            '{}',\
            '{}', \
            '{}', \
            '{}',\
            '{}',\
            '{}',\
            '{}', \
            '{}',\
            '{}', \
            '{}', \
            '{}',\
            '{}',\
            '{}',\
            '{}', \
            '{}',\
            '{}', \
            '{}', \
            '{}',\
            '{}',\
            '{}',\
            '{}', \
            '{}',\
            '{}', \
            '{}', \
            '{}',\
            '{}',\
            '{}',\
            '{}', \
            '{}',\
            '{}', \
            '{}', \
            '{}',\
            '{}',\
            '{}',\
            '{}', \
            '{}',\
            '{}',\
            '{}', \
            '{}',\
            '{}',\
            '{}',\
            '{}', \
            '{}',\
            '{}', \
            '{}', \
            '{}',\
            '{}',\
            '{}',\
            '{}', \
            '{}',\
            '{}', \
            '{}', \
            '{}',\
            '{}',\
            '{}')".format(
            QuestionID,  #QuestionID\
            "0",  #WebSiteID\
            "0",  #SubjectID\
            SubjectName,  #SubjectName\
            "0",  #TextBookID\
            "0",  #TextBookName\
            "0",  #DBCode\
            "0",  #TypeID\
            TypeName,  #TypeName\
            "0",  #BaseTypeID\
            "0",  #QuestionDifficult\
            "0",  #QuestionDifferent\
            "0",  #QuestionDegrade\
            "0",  #QuestionCommand\
            "0",  #QuestionStandard\
            "0",  #QuestionTitle\
            "0",  #QuestionKeyword\
            "0",  #QuestionsSource\
            "0",  #QuestionText\
            "0",  #QuestionTextControl\
            "0",  #QuestionSubNum\
            "0",  #AnswerText\
            "0",  #AnswerControl\
            "0",  #AnswerAnalysisText\
            "0",  #AnalysisControl\
            "0",  #AnnexText\
            "0",  #AnnexControl\
            "0",  #Secrecy\
            "0",  #AnswerTime\
            "0",  #Score\
            "0",  #PointCode\
            "0",  #PointName\
            "0",  #DictionaryCode\
            "0",  #DictionaryName\
            "0",  #UsedTime\
            "0",  #ModifiedDifficulty\
            "0",  #ModifiedDiscrimination\
            "0",  #MarkingMachine\
            "0",  #MarkingMachineValue\
            "0",  #CompanyID\
            "0",  #CheckLink\
            "0",  #CheckNumber\
            "0",  #Price\
            "0",  #PriceUnit\
            "0",  #PriceStatus\
            "0",  #IsUse\
            "0",  #DeleteFlag\
            "0",  #UpdateCount\
            "0",  #CreateUser\
            "0",  #UpdateUser\
            "0",  #QuestionIDCopy\
            "0",  #Click\
            "0",  #GradeCode\
            "0",  #GradeName\
            "0",  #GradeBookCode\
            "0",  #GradeBookName\
            "0",  #questionid1\
            "0",  #questionidcopy1\
            "0",  #AnnexTextCopy1\
            "0",  #AnnexTextCopy2\
            "0",  #ContentMark\
            FilePath,  #FilePath\
            "0",  #QuestionIDCopyHistory\
            "0",  #OrderNum\
            "0",  #ShiTiShow1\
            ShiTiAnswer,  #ShiTiAnswer\
            ShiTiAnalysis,  #ShiTiAnalysis\
            ShiTiShow,  #ShiTiShow\
            "0",  #CreateUserBack\
            "0",  #UnitID\
            "0",  #ShareTag\
            "0",  #DistrictCode\
            "0",  #ChannelName\
            "0",  #ChannelCode\
            "0",  #ProductID\
            "0",  #IsPortalShow\
            "0",  #checkUser\
            "10017",  #agentId\
            "0",  #editAnswerFlag\
            "0")  #solved) \
    return sql_insert

def update(QuestionID, ShiTiAnalysis, ShiTiAnswer):
    sql_str = "update TK_QuestionInfo set ShiTiAnalysis='{}', ShiTiAnswer='{}' where QuestionId='{}'".format(
        ShiTiAnalysis,
        ShiTiAnswer,
        QuestionID
    )
    return sql_str