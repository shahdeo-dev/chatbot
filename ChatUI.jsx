import React, { useState } from 'react';
import axios from 'axios';

function ChatUI() {
  const [file, setFile] = useState(null);
  const [question, setQuestion] = useState('');
  const [answer, setAnswer] = useState('');
  const [loading, setLoading] = useState(false);

  const handleAsk = async () => {
    if (!file || !question) return;
    setLoading(true);
    const form = new FormData();
    form.append('file', file);
    form.append('question', question);
    try {
      const res = await axios.post('http://localhost:8080/ask', form);
      setAnswer(res.data.answer);
    } catch (err) {
      console.error(err);
      setAnswer('Error getting response.');
    }
    setLoading(false);
  };

  return (
    <div className="container p-4">
      <h3>PDF Q&A Chatbot</h3>
      <input type="file" accept="application/pdf" onChange={e => setFile(e.target.files[0])} />
      <input type="text" placeholder="Ask a question..." value={question}
        onChange={e => setQuestion(e.target.value)} className="form-control my-2" />
      <button onClick={handleAsk} disabled={loading} className="btn btn-primary">
        {loading ? 'Processing...' : 'Submit'}
      </button>
      {answer && <div className="mt-4"><strong>Answer:</strong><p>{answer}</p></div>}
    </div>
  );
}

export default ChatUI;
