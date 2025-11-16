import React, { useState, useEffect, useCallback } from "react";
import { Link } from "react-router-dom";
import {
  getAllClients,
  createClient,
  updateClient,
  deleteClient,
  exportClients,
} from "../services/apiClient";

function ClientsPage() {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [formData, setFormData] = useState({
    companyName: "",
    industry: "",
    address: "",
  });
  const [editingClientId, setEditingClientId] = useState(null);

  const [searchCriteria, setSearchCriteria] = useState({
    companyName: "",
    industry: "",
  });
  const [exportLoading, setExportLoading] = useState(false);

  const fetchClients = async (criteria = searchCriteria) => {
    try {
      setLoading(true);
      setError("");
      const response = await getAllClients(criteria);
      setClients(response.data.clients ?? []);
    } catch (err) {
      setError("Failed to load clients.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchClients();
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
    fetchClients();
  };

  const resetForm = () => {
    setFormData({ companyName: "", industry: "", address: "" });
    setEditingClientId(null);
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!formData.companyName || !formData.industry || !formData.address) {
      setError("Please fill all fields.");
      return;
    }

    try {
      if (editingClientId) {
        await updateClient(editingClientId, formData);
      } else {
        await createClient(formData);
      }
      resetForm();
      fetchClients();
    } catch (err) {
      setError(
        editingClientId
          ? "Failed to update client."
          : "Failed to create client."
      );
      console.error(err);
    }
  };

  const handleDeleteClient = async (id) => {
    if (window.confirm("Are you sure?")) {
      try {
        await deleteClient(id);
        fetchClients();
      } catch (err) {
        setError("Failed to delete client.");
        console.error(err);
      }
    }
  };

  const handleEditClick = (client) => {
    setEditingClientId(client.id);
    setFormData({
      companyName: client.companyName,
      industry: client.industry,
      address: client.address,
    });
  };

  const handleExport = async (format) => {
    setExportLoading(true);
    setError("");

    try {
      const response = await exportClients(searchCriteria, format);
      const header = response.headers["content-disposition"];
      const extension = format === "excel" ? ".xlsx" : ".pdf";
      let filename = `clients${extension}`;

      const url = window.URL.createObjectURL(
        new Blob([response.data], { type: response.headers["content-type"] })
      );

      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", filename);
      document.body.appendChild(link);
      link.click();

      link.parentNode.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (err) {
      setError("Failed to export data.");
      console.error(err);
    } finally {
      setExportLoading(false);
    }
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;

  return (
    <div>
      <h2>Clients List</h2>

      <div
        style={{
          border: "1px dashed #ccc",
          padding: "10px",
          marginBottom: "20px",
        }}
      >
        <h3>Search Clients</h3>
        <form onSubmit={handleSearchSubmit}>
          <input
            name="companyName"
            type="text"
            placeholder="Search by Company Name"
            value={searchCriteria.companyName}
            onChange={handleSearchChange}
          />
          <input
            name="industry"
            type="text"
            placeholder="Search by Industry"
            value={searchCriteria.industry}
            onChange={handleSearchChange}
            style={{ marginLeft: "10px" }}
          />
          <button type="submit" style={{ marginLeft: "10px" }}>
            Search
          </button>
          <button
            type="button"
            onClick={() => {
              setSearchCriteria({ companyName: "", industry: "" });
              fetchClients({});
            }}
            style={{ marginLeft: "5px" }}
          >
            Reset
          </button>
          <button
            onClick={() => handleExport("excel")}
            disabled={exportLoading}
          >
            {exportLoading ? "Exporting..." : "Export to Excel"}
          </button>
          <button
            onClick={() => handleExport("pdf")}
            disabled={exportLoading}
            style={{ marginLeft: "10px" }}
          >
            {exportLoading ? "Exporting..." : "Export to PDF"}
          </button>
        </form>
      </div>

      <hr />
      <h3>{editingClientId ? "Edit Client" : "Create New Client"}</h3>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Company Name: </label>
          <input
            name="companyName"
            type="text"
            value={formData.companyName}
            onChange={handleInputChange}
            required
          />
        </div>
        <div>
          <label>Industry: </label>
          <input
            name="industry"
            type="text"
            value={formData.industry}
            onChange={handleInputChange}
            required
          />
        </div>
        <div>
          <label>Address: </label>
          <input
            name="address"
            type="text"
            value={formData.address}
            onChange={handleInputChange}
            required
          />
        </div>
        <br />
        <button type="submit">
          {editingClientId ? "Save Changes" : "Create Client"}
        </button>
        {editingClientId && (
          <button type="button" onClick={resetForm}>
            Cancel
          </button>
        )}
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <hr />

      <table border="1">
        <thead>
          <tr>
            <th>ID</th>
            <th>Company Name</th>
            <th>Industry</th>
            <th>Address</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {clients.map((client) => (
            <tr key={client.id}>
              <td>{client.id}</td>
              <td>{client.companyName}</td>
              <td>{client.industry}</td>
              <td>{client.address}</td>
              <td>
                <button onClick={() => handleEditClick(client)}>Edit</button>
                <button
                  onClick={() => handleDeleteClient(client.id)}
                  style={{ marginLeft: "5px" }}
                >
                  Delete
                </button>

                <Link to={`/client/${client.id}/contacts`}>
                  <button style={{ marginLeft: "5px" }}>View Contacts</button>
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default ClientsPage;
