import firebase_admin
import hashlib
import os
import pyshorteners
import pytz
from datetime import datetime
from firebase_admin import credentials, firestore, storage

service_account_info = {
    "type": "service_account",
    "project_id": "quizapp-e69d7",
    "private_key_id": "5fcd65d8db7603bda682fd369d3170012e031f13",
    "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDS7zJGLrYVbaJs\n2frKVuk99KupBMY0HAPHBBt6Mye07EddMV7Aur9ivblwImHeG57Hl2cxT5T2Hhhq\nQddk1m2/LYQqajuW6aerpuRrOwA+gaFYA1g6M1XDYTV6mC2ZliCW3YEXrMJsoqTu\ng0TYsd1SgmNS8sERekVI7YTN/ZUAWjhwJmluJ1jLyUU0g1INE4QLT7miNu86hxoh\nP7SUCz4hvACYBgLdaRbg2qStashzZ44d+3KQHxLem2ZhqrLi+VogWkhSrYZ31X4F\nRq0AYXgqD59cKnobkPy8qUCSCrg8ABZPCtxprWg3wMe5A3S4Ay2CUX0Noi3QbIBn\nDvvkdbZDAgMBAAECggEAAXBOOHpTFZxU0kc+Vaj4GEsV7KYyr+JNCEt34IJ+z9YY\nuoL+sXPUoFjI7gHeBwTLNXNL6eMwEemsovTmAjMQNpCZ6SwmUeYwFZSi1DRn0QVT\nU2VacbMt75I3KinljbmfaUgDfGTaQqFtXI8aphu4dbqb8NHlDTh3RoMQN9N187Wm\npUfINSBbm4/YDcxE/rwwvxE79At6VMHsbu4fMM5WcDRufF3vBvFI5cErKXTAQWji\nw0kImNDcaw14oIdATfxd6Alb3ST9ImGgJg39HRwq8eH5woTRKKOQk2maPUu6tGj2\n0pGrpgvtL3XHZwVNGfsaw7UZRxahNPoMGY103UORGQKBgQD5v7qgpSdp1cMTcSaP\now9MvefD9gdxqiAKFEJ9UCAlMAweTD+LRRCGq4ux64ot0Inn+RJgorysNXFBE2Y9\nmPMF6kYJMlp6xpEBDHTcwqaQk6D1kQodZ/gYop8SN6Xx0/uOvC4yH8eTYw+woi06\nnwzMtdGDotLwJxIkeSZDbtUIrwKBgQDYNsMhCNGyTNEnFjg5iNYfrHyQt2SJdMRN\n/KKos93JBvOj/IBfCiLSDAkgN1UHW8TsfTlRAuKNp4KGqor/LQE8f85u8gT/dCAa\npkVnYROkjVCDti24EtMvI6x02rKpsPDsozAn354cMqLZ7KBSj/erW8FFeuX64VUp\nMAZDNvSorQKBgAwNt9km/2b5PSgEDwNhsExY4gWwKx3OQyuZXLgllNeWoQ8ZSPvU\nwn5taxmUL6AUb4N1nvBxCkAZeDTW1dllN11yuBybhIhmavss+brp3hYeOeeAL4fJ\ny8yMlLbRXr30KDD0XhpnND+lyU0SFrK8mGaM4+bBhgK3MAX4nuMkRz3bAoGAO0Pn\ngKqWMDV2LyycBMlJSC1Th6d9W2AyXt4dB/kllfDd39laprJ0kPbTmY0RNY8mD1zT\n1SzhRMrhsGAW8ZM4f4jY4Phd/leqZSOmaovlgnlp1HCfX5FpacZxgyZfSrxBjdu9\nW3CkOj52/R0rZPYpgUMJWy4w0+/oE/4h+VKbBZECgYEAxSGgb20K0Tjx6e1hqpk8\nD69Ic2Us/vYTvu+SDKLNnlJO8ZiRBljCtEN9twyMy/6/+MBy6/VPMirrNzr4zeol\nFhatVE/vEwhPFVnQxcau1ZXSSJlzMCmgbgev5d2TVTFG7JSdbJTZPR+gkUQT0HTv\nERgtpCvmMD3+OZhPoH7CN3c=\n-----END PRIVATE KEY-----\n",
    "client_email": "firebase-adminsdk-gzvp9@quizapp-e69d7.iam.gserviceaccount.com",
    "client_id": "114312792834283930991",
    "auth_uri": "https://accounts.google.com/o/oauth2/auth",
    "token_uri": "https://oauth2.googleapis.com/token",
    "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
    "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-gzvp9%40quizapp-e69d7.iam.gserviceaccount.com",
    "universe_domain": "googleapis.com"
}

# Initialize the Firebase app with your service account credentials
cred = credentials.Certificate(service_account_info)

firebase_admin.initialize_app(cred, {
    'storageBucket': 'quizapp-e69d7.appspot.com'
})

# Get a reference to the Firestore client
db = firestore.client()


def check_file_paths(file_paths):
    for file_path in file_paths:
        if os.path.isfile(file_path):
            return file_path

    return None


def update_document(collection_name, document_id, updated_data):
    """
    Update a document in Firestore.

    Args:
        collection_name (str): The name of the Firestore collection.
        document_id (str): The ID of the document to update.
        updated_data (dict): The data to update in the document.
    """
    # Define the document reference you want to update
    doc_ref = db.collection(collection_name).document(document_id)

    # Update the document with the new data
    doc_ref.update(updated_data)

    print("Document updated successfully!")


def upload_apk_to_storage(local_file_path, destination_blob_name):
    """
    Upload an APK file to Firebase Storage.

    Args:
        local_file_path (str): The path to the local APK file.
        destination_blob_name (str): The name of the destination blob in Firebase Storage.
    """
    # Get a reference to the storage bucket
    bucket = storage.bucket()

    # Create a new blob (file) in the bucket
    blob = bucket.blob(destination_blob_name)

    # Compute the SHA256 hash code of the APK file
    with open(local_file_path, "rb") as f:
        bytes = f.read()  # read entire file as bytes
        readable_hash = hashlib.sha256(bytes).hexdigest()

    metadata = {"hashcode": readable_hash}
    blob.metadata = metadata

    # Upload the local file to Firebase Storage
    blob.upload_from_filename(local_file_path)
    blob.make_public()

    print(f"File {local_file_path} uploaded to {destination_blob_name}.")

    # Return the download URL and SHA256 hash code of the uploaded APK file
    return blob.public_url, readable_hash


def create_short_link(long_url):
    """
    Create a shortened URL using pyshorteners.

    Args:
        long_url (str): The long URL to shorten.
    """
    s = pyshorteners.Shortener()
    return s.tinyurl.short(long_url)


if __name__ == "__main__":
    # Get the current user's home directory
    home_dir = os.path.expanduser("~")

    # Construct the file path
    file_path = os.path.join(home_dir, "Documents/GitHub/SoftEdu/app/release/app-release.apk")

    local_file_path = file_path
    destination_blob_name = "SoftEdu.apk"
    document_id = "latest"

    # Upload an APK file to Firebase Storage
    download_url, hash_code = upload_apk_to_storage(local_file_path, destination_blob_name)
    short_url = create_short_link(download_url)

    # Get the current date and time in UTC+3
    tz = pytz.timezone('Europe/Istanbul')  # Istanbul is in the UTC+3 timezone
    current_time = datetime.now(tz)

    # Update a Firestore document
    updated_data = {
        "downloadLink": download_url,
        "shortLink": short_url,
        "hashCode": hash_code,
        "timestamp": current_time
    }
    collection_name = "apks"
    update_document(collection_name, document_id, updated_data)
