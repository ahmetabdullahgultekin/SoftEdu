import hashlib
import argparse

import firebase_admin
from firebase_admin import credentials, firestore, storage
import pyshorteners

# Initialize the Firebase app with your service account credentials
cred = credentials.Certificate("serviceAccountKey.json")
firebase_admin.initialize_app(cred, {
    'storageBucket': 'quizapp-e69d7.appspot.com'
})

# Get a reference to the Firestore client
db = firestore.client()


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

    # Upload the local file to Firebase Storage
    blob.upload_from_filename(local_file_path)
    blob.make_public()

    print(f"File {local_file_path} uploaded to {destination_blob_name}.")
    # Compute the SHA256 hash code of the APK file
    with open(local_file_path, "rb") as f:
        bytes = f.read()  # read entire file as bytes
        readable_hash = hashlib.sha256(bytes).hexdigest()

    # Return the download URL and SHA256 hash code of the uploaded APK file
    return blob.public_url, readable_hash


def create_short_link(long_url):
    """
    Create a shortened URL using pyshorteners.

    Args:
        long_url (str): The long URL to shorten.
    """
    s = pyshorteners.Shortener()
    short_url = s.tinyurl.short(long_url)
    return short_url


if __name__ == "__main__":
    # Create the parser
    parser = argparse.ArgumentParser(
        description="Upload an APK file to Firebase Storage and update a Firestore document.")

    # Add the arguments
    parser.add_argument('local_file_path', type=str, help='The path to the local APK file.')
    parser.add_argument('destination_blob_name', type=str, help='The name of the destination blob in Firebase Storage.')
    parser.add_argument('document_id', type=str, help='The ID of the document to update.')

    # Parse the arguments
    args = parser.parse_args()

    # Upload an APK file to Firebase Storage
    download_url, hash_code = upload_apk_to_storage(args.local_file_path, args.destination_blob_name)
    short_url = create_short_link(download_url)

    # Update a Firestore document
    updated_data = {
        "downloadLink": download_url,
        "shortLink": short_url,
        "hashCode": hash_code,
    }
    collection_name = "apks"
    update_document(collection_name, args.document_id, updated_data)
