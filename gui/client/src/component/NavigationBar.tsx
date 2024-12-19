// @ts-types="npm:@types/react-bootstrap/Container"
import { Container } from "npm:react-bootstrap/Container";
// @ts-types="npm:@types/react-bootstrap/Nav"
import { Nav } from "npm:react-bootstrap/Nav";
// @ts-types="npm:@types/react-bootstrap/Navbar"
import { Navbar } from "npm:react-bootstrap/Navbar";

function NavigationBar() {
  return (
    <Navbar expand="lg" className="navbar navbar-light">
      <Container>
        <Navbar.Brand href="#order">fosscut</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="#order">Order</Nav.Link>
            <Nav.Link href="#gen">Generation</Nav.Link>
            <Nav.Link href="#plan">Plan</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default NavigationBar;
