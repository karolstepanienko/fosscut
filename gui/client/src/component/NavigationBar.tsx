// @ts-types="npm:@types/react-bootstrap/Container"
import { Container } from "npm:react-bootstrap/Container";
// @ts-types="npm:@types/react-bootstrap/Navbar"
import { Navbar } from "npm:react-bootstrap/Navbar";

function NavigationBar() {
  return (
    <Navbar expand="lg" className="navbar navbar-light">
      <Container>
        <Navbar.Brand href="">fosscut</Navbar.Brand>
      </Container>
    </Navbar>
  );
}

export default NavigationBar;
