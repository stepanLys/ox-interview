import React, { useState, useEffect, useCallback } from "react";
import {
  getContactsForClient,
  createContact,
  updateContact,
  deleteContact,
} from "../services/apiClient";
import { useParams, Link } from "react-router-dom";

function ContactsPage() {
  const [contacts, setContacts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const { clientId } = useParams();

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
  });
  const [searchCriteria, setSearchCriteria] = useState({
    firstName: "",
    email: "",
  });
  const [editingContactId, setEditingContactId] = useState(null);

  const fetchContacts = 
    async (criteria = searchCriteria) => {
      try {
        setLoading(true);
        setError("");
        const response = await getContactsForClient(clientId, criteria);
        setContacts(response.data.contacts);
      } catch (err) {
        setError("Failed to load contacts for this client.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

  useEffect(() => {
    fetchContacts();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSearchChange = (e) => {
    const { name, value } = e.target;
    setSearchCriteria((prev) => ({ ...prev, [name]: value }));
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    fetchContacts();
  };

  const resetForm = () => {
    setFormData({ firstName: "", lastName: "", email: "", phone: "" });
    setEditingContactId(null);
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      if (editingContactId) {
        await updateContact(editingContactId, {
          ...formData,
          clientId: Number(clientId),
        });
      } else {
        await createContact({ ...formData, clientId: Number(clientId) });
      }
      resetForm();
      fetchContacts();
    } catch (err) {
      setError("Failed to save contact.");
      console.error(err);
    }
  };

  const handleEditClick = (contact) => {
    setEditingContactId(contact.id);
    setFormData({
      firstName: contact.firstName,
      lastName: contact.lastName,
      email: contact.email,
      phone: contact.phone,
    });
  };

  const handleDeleteClick = async (id) => {
    if (window.confirm("Are you sure you want to delete this contact?")) {
      try {
        await deleteContact(id);
        fetchContacts();
      } catch (err) {
        setError("Failed to delete contact.");
        console.error(err);
      }
    }
  };

  if (loading) return <p>Loading contacts...</p>;

  return (
    <div>
      <h2>Contacts for Client #{clientId}</h2>

      <div
        style={{
          border: "1px dashed #ccc",
          padding: "10px",
          marginBottom: "20px",
        }}
      >
        <h3>Search Contacts</h3>
        <form onSubmit={handleSearchSubmit}>
          <input
            name="firstName"
            type="text"
            placeholder="Search by First Name"
            value={searchCriteria.firstName}
            onChange={handleSearchChange}
          />
          <input
            name="email"
            type="text"
            placeholder="Search by Email"
            value={searchCriteria.email}
            onChange={handleSearchChange}
            style={{ marginLeft: "10px" }}
          />
          <button type="submit" style={{ marginLeft: "10px" }}>
            Search
          </button>
          <button
            type="button"
            onClick={() => {
              setSearchCriteria({ firstName: "", email: "" });
              fetchContacts({});
            }}
            style={{ marginLeft: "5px" }}
          >
            Reset
          </button>
        </form>
      </div>
      <h3>{editingContactId ? "Edit Contact" : "Create New Contact"}</h3>

      <form onSubmit={handleSubmit}>
        <div>
          <label>First Name:</label>
          <input
            name="firstName"
            value={formData.firstName}
            onChange={handleInputChange}
            required
          />
        </div>
        <div>
          <label>Last Name:</label>
          <input
            name="lastName"
            value={formData.lastName}
            onChange={handleInputChange}
            required
          />
        </div>
        <div>
          <label>Email:</label>
          <input
            name="email"
            value={formData.email}
            onChange={handleInputChange}
            type="email"
          />
        </div>
        <div>
          <label>Phone:</label>
          <input
            name="phone"
            value={formData.phone}
            onChange={handleInputChange}
          />
        </div>

        <button type="submit">
          {editingContactId ? "Update Contact" : "Create Contact"}
        </button>
        {editingContactId && (
          <button
            type="button"
            onClick={resetForm}
            style={{ marginLeft: "10px" }}
          >
            Cancel
          </button>
        )}
      </form>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <hr />

      <table border="1">
        <thead>
          <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {contacts.map((contact) => (
            <tr key={contact.id}>
              <td>{contact.firstName}</td>
              <td>{contact.lastName}</td>
              <td>{contact.email}</td>
              <td>{contact.phone}</td>
              <td>
                <button onClick={() => handleEditClick(contact)}>Edit</button>
                <button onClick={() => handleDeleteClick(contact.id)}>
                  Delete
                </button>
                <Link to={`/contacts/${contact.id}/tasks`}>
                  <button style={{ marginLeft: "5px" }}>View Tasks</button>
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default ContactsPage;
