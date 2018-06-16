module HttpActions exposing (getMeasureList, getMeasure)

import Http
import Json.Decode as Json exposing (at, decodeString, int, list, string, succeed)
import Json.Decode exposing (int, string, float, Decoder)
import Json.Decode.Pipeline exposing (decode, hardcoded, optional, required, requiredAt)
import Messages exposing (Msg(GetMeasure, GetMeasureList))
import Models exposing (Drag, Measure, MeasureItem, Step)

getMeasureList : Cmd Msg
getMeasureList =
    let
        url = "/measure_list"
        request = Http.get url (Json.list measureListItemDecoder)
    in
        Http.send GetMeasureList request

measureListItemDecoder: Decoder MeasureItem
measureListItemDecoder = decode MeasureItem
    |> required "measureId" int
    |> required "measureName" string

getMeasure : Int -> Cmd Msg
getMeasure id =
    let
        url = String.append "/measure?id=" (toString(id))
        request = Http.get url measureDecoder
    in
        Http.send GetMeasure request

measureDecoder: Decoder Measure
measureDecoder = decode Measure
    |> required "measureId" int
    |> required "measureName" string
    |> requiredAt ["measureJson","description"] string
    |> requiredAt ["measureJson","steps"] stepsDecoder
    |> hardcoded Nothing

stepsDecoder: Json.Decoder (List Step)
stepsDecoder = Json.list stepDecoder

stepDecoder: Decoder Step
stepDecoder = decode Step
    |> required "stepId" int
    |> required "rule" string
    |> required "successStepId" int
    |> required "failureStepId" int
    |> hardcoded False