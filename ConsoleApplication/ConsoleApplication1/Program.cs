using Microsoft.ProjectOxford.Face;
using Microsoft.ProjectOxford.Face.Contract;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApplication1
{
    class Program
    {
        static FaceServiceClient faceServiceClient = new FaceServiceClient("73a339d4f6484ff1aa6a2e9fc14d89a2", "https://eastasia.api.cognitive.microsoft.com/face/v1.0");
        public static async void CreatePersonGroup(string personGroupId, string personGroupName)
        {
            try
            {
                await faceServiceClient.CreatePersonGroupAsync(personGroupId, personGroupName);
                Console.WriteLine("Create Person Group succeed");
            }
            catch (Exception ex)
            {
                Console.WriteLine("[Error ] " + ex.Message);
            }
        }
        public static async void AddPersonToGroup(string personGroupId, string personName, string imgPath)
        {
            try
            {
                await faceServiceClient.GetPersonGroupAsync(personGroupId);
                CreatePersonResult personResult = await faceServiceClient.CreatePersonAsync(personGroupId, personName);
                DetectFaceAndRegister(personGroupId, personResult, imgPath);
            }
            catch (Exception ex)
            {
                Console.WriteLine("[Error] " + ex.Message);
            }
        }
        private static async void DetectFaceAndRegister(string personGroupId, CreatePersonResult personResult, string imgPath)
        {
            foreach (var image in Directory.GetFiles(imgPath, "*.jpg"))//only store images of .jpg format in this folder
            {
                using (Stream s = File.OpenRead(image))
                {
                    try
                    {
                        await faceServiceClient.AddPersonFaceAsync(personGroupId, personResult.PersonId, s);
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine("[Error] " + ex.Message);
                    }
                }
            }
            Console.WriteLine("Add images to " + personGroupId + " group succeeded");
        }
        private static async void TraiingAI(string personGroupId)
        {
            await faceServiceClient.TrainPersonGroupAsync(personGroupId);
            TrainingStatus training = null;
            while (true)
            {
                training = await faceServiceClient.GetPersonGroupTrainingStatusAsync(personGroupId);
                if (training.Status != Status.Running)
                {
                    Console.WriteLine("Status: " + training.Status);
                    break;
                }
                Console.WriteLine("Waiting for training AI...");
                await Task.Delay(1000);//delay for 1 second
            }
            Console.WriteLine("Training AI completed");
        }
        private static async void IdentifyFace(string personGroupId, string imgPath)
        {
            using (Stream s = File.OpenRead(imgPath))
            {
                var faces = await faceServiceClient.DetectAsync(s);//detect face range from picture
                var faceIds = faces.Select(face => face.FaceId).ToArray(); //Get all faceIds to array

                try
                {
                    var results = await faceServiceClient.IdentifyAsync(personGroupId, faceIds);
                    foreach (var identifyResult in results)
                    {
                        Console.WriteLine($"Result of face: {identifyResult.FaceId}");
                        if (identifyResult.Candidates.Length == 0)
                            Console.WriteLine("No one identified");
                        else
                        {
                            var candidateId = identifyResult.Candidates[0].PersonId;
                            var person = await faceServiceClient.GetPersonAsync(personGroupId, candidateId);
                            Console.WriteLine($"Identified as {person.Name}");
                        }
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine("[Error] " + ex.Message);
                }
            }
        }

        static void Main(string[] args)
        {

            //CreatePersonGroup("bscs6a", "CS6A");

            //AddPersonToGroup("bscs6a", "198005", @"C:\AP\bscs6a\Yahya");

            //TraiingAI("bscs6a");

            IdentifyFace("bscs6a", @"C:\Users\Roshan\Desktop\test3.jpg");
            Console.ReadKey();
        }
    }
}
